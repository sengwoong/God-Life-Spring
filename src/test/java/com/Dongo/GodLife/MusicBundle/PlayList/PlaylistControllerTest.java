package com.Dongo.GodLife.MusicBundle.PlayList;

import com.Dongo.GodLife.MusicBundle.PlayList.Controller.PlaylistController;
import com.Dongo.GodLife.MusicBundle.PlayList.Dto.PlaylistRequest;
import com.Dongo.GodLife.MusicBundle.PlayList.Exception.NotYourPlaylistException;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.MusicBundle.PlayList.Service.PlaylistService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class PlaylistControllerTest {

    @Mock
    private PlaylistService playlistService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PlaylistController playlistController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        objectMapper = mapper;
        mockMvc = MockMvcBuilders.standaloneSetup(playlistController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Nested
    class PlaylistManagement {
        @Test
        @DisplayName("새로운 플레이리스트를 생성한다")
        void testCreatePlaylist() throws Exception {
            // given
            User user = User.builder().id(1L).build();
            PlaylistRequest request = new PlaylistRequest();
            request.setPlayListTitle("My Playlist");
            request.setImageUrl("http://test.com/image.jpg");
            request.setShared(false);

            Playlist playlist = Playlist.builder()
                    .playlistId(1L)
                    .playlistTitle("My Playlist")
                    .imageUrl("http://test.com/image.jpg")
                    .isShared(false)
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.createPlaylist(any(PlaylistRequest.class), any(User.class))).willReturn(playlist);

            // when & then
            mockMvc.perform(post("/playlists/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.playlistId").value(1))
                    .andExpect(jsonPath("$.playlistTitle").value("My Playlist"));

            verify(playlistService).createPlaylist(any(PlaylistRequest.class), any(User.class));
        }

        @Test
        @DisplayName("사용자의 플레이리스트 목록을 조회한다")
        void testGetUserPlaylists() throws Exception {
            // given
            User user = User.builder().id(1L).build();
            Playlist playlist = Playlist.builder()
                    .playlistId(1L)
                    .playlistTitle("My Playlist")
                    .user(user)
                    .build();
            Page<Playlist> playlistPage = new PageImpl<>(List.of(playlist));

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.getAllPlaylistsByUserId(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.any(Pageable.class))).willReturn(playlistPage);

            // when & then
            mockMvc.perform(get("/playlists/user/1"))
                    .andExpect(status().isOk());

            verify(playlistService).getAllPlaylistsByUserId(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        @DisplayName("플레이리스트를 업데이트한다")
        void testUpdatePlaylist() throws Exception, NotYourPlaylistException {
            // given
            User user = User.builder().id(1L).build();
            PlaylistRequest request = new PlaylistRequest();
            request.setPlayListTitle("Updated Playlist");
            request.setImageUrl("http://test.com/updated.jpg");
            request.setShared(true);

            Playlist updatedPlaylist = Playlist.builder()
                    .playlistId(1L)
                    .playlistTitle("Updated Playlist")
                    .imageUrl("http://test.com/updated.jpg")
                    .isShared(true)
                    .user(user)
                    .build();

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.updatePlayList(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.eq(1L), any(PlaylistRequest.class))).willReturn(updatedPlaylist);

            // when & then
            mockMvc.perform(put("/playlists/playlist/1/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.playlistTitle").value("Updated Playlist"));

            verify(playlistService).updatePlayList(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.eq(1L), any(PlaylistRequest.class));
        }

        @Test
        @DisplayName("플레이리스트를 삭제한다")
        void testDeletePlaylist() throws Exception, NotYourPlaylistException {
            // given
            User user = User.builder().id(1L).build();
            given(userService.CheckUserAndGetUser(1L)).willReturn(user);

            // when & then
            mockMvc.perform(delete("/playlists/playlist/1/user/1"))
                    .andExpect(status().isNoContent());

            verify(playlistService).deletePlayList(1L, 1L);
        }
    }

    @Nested
    class PlaylistSearch {
        @Test
        @DisplayName("플레이리스트를 검색한다")
        void testSearchPlaylists() throws Exception {
            // given
            User user = User.builder().id(1L).build();
            Playlist playlist = Playlist.builder()
                    .playlistId(1L)
                    .playlistTitle("Rock Playlist")
                    .user(user)
                    .build();
            Page<Playlist> playlistPage = new PageImpl<>(List.of(playlist));

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.getAllPlaylistsByUserIdWithSearch(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.eq("Rock"), org.mockito.ArgumentMatchers.any(Pageable.class))).willReturn(playlistPage);

            // when & then
            mockMvc.perform(get("/playlists/user/1").param("search", "Rock"))
                    .andExpect(status().isOk());

            verify(playlistService).getAllPlaylistsByUserIdWithSearch(org.mockito.ArgumentMatchers.any(User.class), org.mockito.ArgumentMatchers.eq("Rock"), org.mockito.ArgumentMatchers.any(Pageable.class));
        }
    }
} 