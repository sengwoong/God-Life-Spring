package com.Dongo.GodLife.MusicBundle.PlayList;

import com.Dongo.GodLife.MusicBundle.PlayList.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistPersistenceAdapter;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTests {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistPersistenceAdapter playlistRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("test@test.com");
        user1.setId(1L);
        user2 = new User("other@test.com");
        user2.setId(2L);
    }

    @Nested
    class CreatePlaylist {
        @Test
        @DisplayName("새로운 Playlist를 생성하고 저장한다")
        void createPlaylist_Success() {
            // given
            PlaylistRequest playlistRequest = new PlaylistRequest("My Playlist");

            Playlist playlist = new Playlist();
            playlist.setPlaylistTitle("My Playlist");

            when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

            // when
            Playlist result = playlistService.createPlaylist(playlistRequest, user1);

            // then
            assertNotNull(result);
            assertEquals("My Playlist", result.getPlaylistTitle());
            verify(playlistRepository).save(any(Playlist.class));
        }
    }

    @Nested
    class GetAllPlaylistsByUserId {
        @Test
        @DisplayName("사용자의 모든 Playlist를 조회한다")
        void getAllPlaylistsByUserId_Success() {
            // given
            Playlist playlist = new Playlist();
            playlist.setPlaylistTitle("My Playlist");
            Page<Playlist> playlistPage = new PageImpl<>(Collections.singletonList(playlist));

            when(playlistRepository.findByUser(any(User.class), any(Pageable.class)))
                    .thenReturn(playlistPage);

            // when
            Page<Playlist> result = playlistService.getAllPlaylistsByUserId(user1, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("My Playlist", result.getContent().get(0).getPlaylistTitle());
        }
    }

    @Nested
    class UpdatePlaylist {
        @Test
        @DisplayName("Playlist를 성공적으로 수정한다")
        void updatePlaylist_Success() throws NotYourPlaylistException {
            // given
            Playlist existingPlaylist = new Playlist();
            existingPlaylist.setUser(user1);
            
            PlaylistRequest playlistRequest = new PlaylistRequest("Updated Playlist");

            when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(existingPlaylist));
            when(playlistRepository.save(any(Playlist.class))).thenReturn(existingPlaylist);

            // when
            Playlist result = playlistService.updatePlayList(1L, user1.getId(), playlistRequest);

            // then
            assertNotNull(result);
            assertEquals("Updated Playlist", result.getPlaylistTitle());
        }

        @Test
        @DisplayName("다른 사용자의 Playlist를 수정하려고 하면 예외가 발생한다")
        void updatePlaylist_NotYourPlaylist() {
            // given
            Playlist existingPlaylist = new Playlist();
            existingPlaylist.setUser(user1);
            
            PlaylistRequest playlistRequest = new PlaylistRequest("Updated Playlist");
            
            when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(existingPlaylist));

            // when & then
            assertThrows(NotYourPlaylistException.class, 
                () -> playlistService.updatePlayList(1L, user2.getId(), playlistRequest));
        }
    }

    @Nested
    class DeletePlaylist {
        @Test
        @DisplayName("Playlist를 성공적으로 삭제한다")
        void deletePlaylist_Success() throws NotYourPlaylistException {
            // given
            Playlist playlist = new Playlist();
            playlist.setUser(user1);

            when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(playlist));

            // when
            assertDoesNotThrow(() -> playlistService.deletePlaylist(1L, user1.getId()));

            // then
            verify(playlistRepository).delete(any(Playlist.class));
        }

        @Test
        @DisplayName("다른 사용자의 Playlist를 삭제하려고 하면 예외가 발생한다")
        void deletePlaylist_NotYourPlaylist() {
            // given
            Playlist playlist = new Playlist();
            playlist.setUser(user1);

            when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(playlist));

            // when & then
            assertThrows(NotYourPlaylistException.class, 
                () -> playlistService.deletePlaylist(1L, user2.getId()));
        }
    }
} 