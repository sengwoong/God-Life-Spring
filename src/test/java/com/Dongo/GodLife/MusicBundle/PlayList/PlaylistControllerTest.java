package com.Dongo.GodLife.MusicBundle.PlayList;

import com.Dongo.GodLife.MusicBundle.PlayList.Controller.PlaylistController;
import com.Dongo.GodLife.MusicBundle.PlayList.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
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

@WebMvcTest(PlaylistController.class)
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService playlistService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Playlist 관리")
    @Nested
    class PlaylistManagement {

        @DisplayName("Playlist 생성")
        @Nested
        class CreatePlaylist {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 Playlist를 반환한다")
            void createPlaylist_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                PlaylistRequest playlistRequest = new PlaylistRequest("My Playlist");

                Playlist playlist = new Playlist();
                playlist.setPlaylistTitle("My Playlist");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(playlistService.createPlaylist(any(PlaylistRequest.class), any(User.class)))
                        .willReturn(playlist);

                // when & then
                mockMvc.perform(post("/playlists/user/{user_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlistRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.playlistTitle").value("My Playlist"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(playlistService).createPlaylist(any(PlaylistRequest.class), any(User.class));
            }
        }

        @DisplayName("Playlist 조회")
        @Nested
        class GetPlaylists {
            @Test
            @DisplayName("사용자의 Playlist 목록을 조회한다")
            void getPlaylistsByUserId_Success() throws Exception {
                // given
                User user = new User("test@test.com");
                Playlist playlist = new Playlist();
                playlist.setPlaylistTitle("My Playlist");

                Page<Playlist> playlistPage = new PageImpl<>(List.of(playlist));

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(playlistService.getAllPlaylistsByUserId(any(User.class), any()))
                        .willReturn(playlistPage);

                // when & then
                mockMvc.perform(get("/playlists/user/{user_id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].playlistTitle").value("My Playlist"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(playlistService).getAllPlaylistsByUserId(any(User.class), any());
            }
        }

        @DisplayName("Playlist 수정")
        @Nested
        class UpdatePlaylist {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 Playlist를 반환한다")
            void updatePlaylist_Success() throws Exception, NotYourPlaylistException {
                // given
                User user = new User("test@test.com");
                PlaylistRequest playlistRequest = new PlaylistRequest("Updated Playlist");
        
                Playlist updatedPlaylist = new Playlist();
                updatedPlaylist.setPlaylistTitle("Updated Playlist");

                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);
                given(playlistService.updatePlayList(anyLong(), anyLong(), any(PlaylistRequest.class)))
                        .willReturn(updatedPlaylist);

                // when & then
                mockMvc.perform(put("/playlists/playlist/{playlist_id}/user/{user_id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlistRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.playlistTitle").value("Updated Playlist"));

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(playlistService).updatePlayList(anyLong(), anyLong(), any(PlaylistRequest.class));
            }
        }

        @DisplayName("Playlist 삭제")
        @Nested
        class DeletePlaylist  {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deletePlaylist_Success() throws Exception, NotYourPlaylistException {
                // given
                User user = new User("test@test.com");
                given(userService.CheckUserAndGetUser(anyLong())).willReturn(user);

                // when & then
                mockMvc.perform(delete("/playlists/playlist/{playlist_id}/user/{user_id}", 1L, 1L))
                        .andExpect(status().isNoContent());

                verify(userService).CheckUserAndGetUser(anyLong());
                verify(playlistService).deletePlaylist(anyLong(), anyLong());
            }
        }
    }
} 