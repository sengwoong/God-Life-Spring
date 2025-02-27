package com.Dongo.GodLife.MusicBundle.Music;

import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicPersistenceAdapter;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
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
class MusicServiceTests {

    @InjectMocks
    private MusicService musicService;

    @Mock
    private MusicPersistenceAdapter musicRepository;

    private User user;
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        user = new User("test@test.com");
        user.setId(1L);
        playlist = new Playlist();
        playlist.setPlaylistId(1L);
        playlist.setUser(user);
    }

    @Nested
    class CreateMusic {
        @Test
        @DisplayName("새로운 Music을 생성하고 저장한다")
        void createMusic_Success() {
            // given
            MusicRequest musicRequest = new MusicRequest();
            musicRequest.setMusicTitle("Test Song");
            musicRequest.setMusicUrl("http://test.com/song.mp3");

            Music music = new Music();
            music.setMusicTitle("Test Song");
            music.setMusicUrl("http://test.com/song.mp3");

            when(musicRepository.save(any(Music.class))).thenReturn(music);

            // when
            Music result = musicService.createMusic(musicRequest);

            // then
            assertNotNull(result);
            assertEquals("Test Song", result.getMusicTitle());
            assertEquals("http://test.com/song.mp3", result.getMusicUrl());
            verify(musicRepository).save(any(Music.class));
        }
    }

    @Nested
    class GetAllMusicByPlaylist {
        @Test
        @DisplayName("Playlist ID로 모든 Music을 조회한다")
        void getAllMusicByPlaylist_Success() {
            // given
            Music music = new Music();
            music.setMusicTitle("Test Song");
            Page<Music> musicPage = new PageImpl<>(Collections.singletonList(music));

            when(musicRepository.findPlaylistMusics(anyLong(), any(Pageable.class))).thenReturn(musicPage);

            // when
            Page<Music> result = musicService.getAllMusicByPlaylist(1L, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("Test Song", result.getContent().get(0).getMusicTitle());
        }
    }

    @Nested
    class UpdateMusic {
        @Test
        @DisplayName("Music을 성공적으로 수정한다")
        void updateMusic_Success() {
            // given
            Music existingMusic = new Music();
            existingMusic.setPlaylist(playlist);
            
            MusicRequest musicRequest = new MusicRequest();
            musicRequest.setMusicTitle("Updated Song");
            musicRequest.setMusicUrl("http://test.com/updated.mp3");

            when(musicRepository.findById(anyLong())).thenReturn(Optional.of(existingMusic));
            when(musicRepository.save(any(Music.class))).thenReturn(existingMusic);

            // when
            Music result = musicService.updateMusic(1L, user, musicRequest);

            // then
            assertNotNull(result);
            assertEquals("Updated Song", result.getMusicTitle());
            assertEquals("http://test.com/updated.mp3", result.getMusicUrl());
        }

        @Test
        @DisplayName("다른 사용자의 Music을 수정하려고 하면 예외가 발생한다")
        void updateMusic_NotYourMusic() {
            // given
            Music existingMusic = new Music();
            existingMusic.setPlaylist(playlist);
            
            User otherUser = new User("other@test.com");
            otherUser.setId(2L);
            
            MusicRequest musicRequest = new MusicRequest();
            
            when(musicRepository.findById(anyLong())).thenReturn(Optional.of(existingMusic));

            // when & then
            assertThrows(NotYourMusicException.class, 
                () -> musicService.updateMusic(1L, otherUser, musicRequest));
        }
    }

    @Nested
    class DeleteMusic {
        @Test
        @DisplayName("Music을 성공적으로 삭제한다")
        void deleteMusic_Success() {
            // given
            Music music = new Music();
            music.setPlaylist(playlist);

            when(musicRepository.findById(anyLong())).thenReturn(Optional.of(music));

            // when
            assertDoesNotThrow(() -> musicService.deleteMusic(1L, user));

            // then
            verify(musicRepository).delete(any(Music.class));
        }

        @Test
        @DisplayName("다른 사용자의 Music을 삭제하려고 하면 예외가 발생한다")
        void deleteMusic_NotYourMusic() {
            // given
            Music music = new Music();
            music.setPlaylist(playlist);

            User otherUser = new User("other@test.com");
            otherUser.setId(2L);

            when(musicRepository.findById(anyLong())).thenReturn(Optional.of(music));

            // when & then
            assertThrows(NotYourMusicException.class, 
                () -> musicService.deleteMusic(1L, otherUser));
        }
    }
} 