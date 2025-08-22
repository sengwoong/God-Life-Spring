package com.Dongo.GodLife.MusicBundle.Music;

import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Exception.NotYourMusicException;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicPersistenceAdapter;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.User.Model.User;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceTests {

    @Mock
    private MusicPersistenceAdapter musicRepository;

    @Mock
    private com.Dongo.GodLife.MusicBundle.MusicLike.Service.MusicLikeService musicLikeService;

    @InjectMocks
    private MusicService musicService;

    private User user;
    private Playlist playlist;
    private MusicRequest musicRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickName("testuser")
                .build();

        playlist = Playlist.builder()
                .playlistId(1L)
                .playlistTitle("Test Playlist")
                .user(user)
                .build();

        musicRequest = new MusicRequest();
        musicRequest.setMusicTitle("Test Music");
        musicRequest.setMusicUrl("http://test.com/music.mp3");
        musicRequest.setImageUrl("http://test.com/image.jpg");
    }

    @Nested
    class CreateMusic {
        @Test
        @DisplayName("새로운 음악을 생성한다")
        void testCreateMusic() {
            // given
            given(musicRepository.save(any(Music.class))).willAnswer(invocation -> {
                Music music = invocation.getArgument(0);
                music.setMusicId(1L);
                return music;
            });

            // when
            Music result = musicService.createMusic(musicRequest, playlist);

            // then
            assertNotNull(result);
            assertEquals("Test Music", result.getMusicTitle());
            assertEquals("http://test.com/music.mp3", result.getMusicUrl());
            assertEquals("http://test.com/image.jpg", result.getImageUrl());
            assertEquals(playlist, result.getPlaylist());
            verify(musicRepository).save(any(Music.class));
        }

        @Test
        @DisplayName("이미지 URL이 없을 때 기본 이미지를 설정한다")
        void testCreateMusicWithDefaultImage() {
            // given
            musicRequest.setImageUrl(null);
            given(musicRepository.save(any(Music.class))).willAnswer(invocation -> {
                Music music = invocation.getArgument(0);
                music.setMusicId(1L);
                return music;
            });

            // when
            Music result = musicService.createMusic(musicRequest, playlist);

            // then
            assertNotNull(result);
            assertEquals("https://example.com/default-music-image.jpg", result.getImageUrl());
        }
    }

    @Nested
    class GetMusic {
        @Test
        @DisplayName("음악 ID로 음악을 조회한다")
        void testGetMusicById() {
            // given
            Music music = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .musicUrl("http://test.com/music.mp3")
                    .playlist(playlist)
                    .build();
            given(musicRepository.findById(1L)).willReturn(java.util.Optional.of(music));

            // when
            Music result = musicService.getMusicById(1L);

            // then
            assertNotNull(result);
            assertEquals("Test Music", result.getMusicTitle());
            assertEquals("http://test.com/music.mp3", result.getMusicUrl());
        }

        @Test
        @DisplayName("존재하지 않는 음악 ID로 조회하면 예외가 발생한다")
        void testGetMusicByIdNotFound() {
            // given
            given(musicRepository.findById(999L)).willReturn(java.util.Optional.empty());

            // when & then
            assertThrows(jakarta.persistence.EntityNotFoundException.class,
                    () -> musicService.getMusicById(999L));
        }
    }

    @Nested
    class GetMusicByPlaylist {
        @Test
        @DisplayName("플레이리스트의 음악 목록을 조회한다")
        void testGetAllMusicByPlaylist() {
            // given
            Music music1 = Music.builder()
                    .musicId(1L)
                    .musicTitle("Music 1")
                    .musicUrl("http://test.com/music1.mp3")
                    .build();
            Music music2 = Music.builder()
                    .musicId(2L)
                    .musicTitle("Music 2")
                    .musicUrl("http://test.com/music2.mp3")
                    .build();
            Page<Music> musicPage = new PageImpl<>(List.of(music1, music2));
            given(musicRepository.findPlaylistMusics(org.mockito.ArgumentMatchers.eq(1L), any(Pageable.class)))
                    .willReturn(musicPage);

            // when
            Page<Music> result = musicService.getAllMusicByPlaylist(1L, null, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("Music 1", result.getContent().get(0).getMusicTitle());
            assertEquals("Music 2", result.getContent().get(1).getMusicTitle());
        }
    }

    @Nested
    class UpdateMusic {
        @Test
        @DisplayName("음악을 업데이트한다")
        void testUpdateMusic() {
            // given
            Music existingMusic = Music.builder()
                    .musicId(1L)
                    .musicTitle("Old Title")
                    .musicUrl("http://old.com/music.mp3")
                    .playlist(playlist)
                    .build();
            given(musicRepository.findById(1L)).willReturn(java.util.Optional.of(existingMusic));
            given(musicRepository.save(any(Music.class))).willReturn(existingMusic);

            MusicRequest updateRequest = new MusicRequest();
            updateRequest.setMusicTitle("New Title");
            updateRequest.setMusicUrl("http://new.com/music.mp3");

            // when
            Music result = musicService.updateMusic(1L, user, updateRequest);

            // then
            assertNotNull(result);
            assertEquals("New Title", result.getMusicTitle());
            assertEquals("http://new.com/music.mp3", result.getMusicUrl());
            verify(musicRepository).save(any(Music.class));
        }

        @Test
        @DisplayName("다른 사용자의 음악을 업데이트하려고 하면 예외가 발생한다")
        void testUpdateMusicNotOwner() {
            // given
            User otherUser = User.builder().id(2L).email("other@test.com").build();
            Music existingMusic = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .playlist(playlist)
                    .build();
            given(musicRepository.findById(1L)).willReturn(java.util.Optional.of(existingMusic));

            // when & then
            assertThrows(NotYourMusicException.class,
                    () -> musicService.updateMusic(1L, otherUser, musicRequest));
            verify(musicRepository, never()).save(any(Music.class));
        }
    }

    @Nested
    class DeleteMusic {
        @Test
        @DisplayName("음악을 삭제한다")
        void testDeleteMusic() {
            // given
            Music existingMusic = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .playlist(playlist)
                    .build();
            given(musicRepository.findById(1L)).willReturn(java.util.Optional.of(existingMusic));

            // when
            musicService.deleteMusic(1L, user);

            // then
            verify(musicRepository).delete(existingMusic);
        }

        @Test
        @DisplayName("다른 사용자의 음악을 삭제하려고 하면 예외가 발생한다")
        void testDeleteMusicNotOwner() {
            // given
            User otherUser = User.builder().id(2L).email("other@test.com").build();
            Music existingMusic = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .playlist(playlist)
                    .build();
            given(musicRepository.findById(1L)).willReturn(java.util.Optional.of(existingMusic));

            // when & then
            assertThrows(NotYourMusicException.class,
                    () -> musicService.deleteMusic(1L, otherUser));
            verify(musicRepository, never()).delete(any(Music.class));
        }
    }
} 