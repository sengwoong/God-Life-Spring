package com.Dongo.GodLife.MusicBundle.Music;

import com.Dongo.GodLife.MusicBundle.Music.Controller.MusicController;
import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MusicController.class)
class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Music 관리")
    @Nested
    class MusicManagement {

        @DisplayName("Music 생성")
        @Nested
        class CreateMusic {
            @Test
            @DisplayName("정상적인 요청일 경우, Music을 반환한다")
            void createMusic_Success() throws Exception {
                // given
                MusicRequest musicRequest = new MusicRequest();
                musicRequest.setMusicTitle("Test Song");
                musicRequest.setMusicUrl("http://test.com/song.mp3");

                Music music = new Music();
                music.setMusicTitle("Test Song");
                music.setMusicUrl("http://test.com/song.mp3");

                given(musicService.createMusic(any(MusicRequest.class))).willReturn(music);

                // when & then
                mockMvc.perform(post("/musics")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(musicRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.musicTitle").value("Test Song"))
                        .andExpect(jsonPath("$.musicUrl").value("http://test.com/song.mp3"));

                verify(musicService).createMusic(any(MusicRequest.class));
            }
        }

        @DisplayName("Music 조회")
        @Nested
        class GetMusics {
            @Test
            @DisplayName("Playlist의 Music 목록을 조회한다")
            void getMusicsByPlaylistId_Success() throws Exception {
                // given
                Music music = new Music();
                music.setMusicTitle("Test Song");
                music.setMusicUrl("http://test.com/song.mp3");

                Page<Music> musicPage = new PageImpl<>(List.of(music));

                given(musicService.getAllMusicByPlaylist(anyLong(), any())).willReturn(musicPage);

                // when & then
                mockMvc.perform(get("/musics/playlist/{playlist_id}", 1L)
                                .param("page", "0")
                                .param("size", "10"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].musicTitle").value("Test Song"))
                        .andExpect(jsonPath("$.content[0].musicUrl").value("http://test.com/song.mp3"));

                verify(musicService).getAllMusicByPlaylist(anyLong(), any());
            }
        }

        @DisplayName("Music 수정")
        @Nested
        class UpdateMusic {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Music을 반환한다")
            void updateMusic_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                MusicRequest musicRequest = new MusicRequest();
                musicRequest.setMusicTitle("Updated Song");
                musicRequest.setMusicUrl("http://test.com/updated.mp3");

                Music updatedMusic = new Music();
                updatedMusic.setMusicTitle("Updated Song");
                updatedMusic.setMusicUrl("http://test.com/updated.mp3");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(musicService.updateMusic(anyLong(), any(User.class), any(MusicRequest.class)))
                        .willReturn(updatedMusic);

                // when & then
                mockMvc.perform(put("/musics/music/{music_id}/user/{user_id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(musicRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.musicTitle").value("Updated Song"))
                        .andExpect(jsonPath("$.musicUrl").value("http://test.com/updated.mp3"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(musicService).updateMusic(anyLong(), any(User.class), any(MusicRequest.class));
            }
        }

        @DisplayName("Music 삭제")
        @Nested
        class DeleteMusic {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteMusic_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);

                // when & then
                mockMvc.perform(delete("/musics/music/{music_id}/user/{user_id}", 1L, 1L))
                        .andExpect(status().isNoContent());

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(musicService).deleteMusic(anyLong(), any(User.class));
            }
        }
    }
} 