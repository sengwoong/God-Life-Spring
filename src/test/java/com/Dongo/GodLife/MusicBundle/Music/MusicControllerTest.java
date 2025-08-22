package com.Dongo.GodLife.MusicBundle.Music;

import com.Dongo.GodLife.MusicBundle.Music.Controller.MusicController;
import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicRequest;
import com.Dongo.GodLife.MusicBundle.Music.Dto.MusicResponse;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
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
class MusicControllerTest {

    @Mock
    private MusicService musicService;

    @Mock
    private UserService userService;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private MusicController musicController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(musicController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper))
                .build();
        objectMapper = mapper;
    }

    @Nested
    class MusicManagement {
        @Test
        @DisplayName("새로운 음악을 생성한다")
        void testCreateMusic() throws Exception {
            // given
            User user = User.builder().id(1L).build();
            Playlist playlist = Playlist.builder().playlistId(1L).user(user).build();
            MusicRequest request = new MusicRequest();
            request.setMusicTitle("Test Music");
            request.setMusicUrl("http://test.com/music.mp3");
            
            Music music = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .musicUrl("http://test.com/music.mp3")
                    .createdAt(LocalDateTime.now())
                    .build();

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.getPlayListById(1L)).willReturn(playlist);
            given(musicService.createMusic(any(MusicRequest.class), any(Playlist.class))).willReturn(music);

            // when & then
            mockMvc.perform(post("/musics/playlist/1/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.musicId").value(1))
                    .andExpect(jsonPath("$.musicTitle").value("Test Music"))
                    .andExpect(jsonPath("$.musicUrl").value("http://test.com/music.mp3"));

            verify(musicService).createMusic(any(MusicRequest.class), any(Playlist.class));
        }

        @Test
        @DisplayName("플레이리스트의 음악 목록을 조회한다")
        void testGetMusicByPlaylist() throws Exception {
            // given
            User user = User.builder().id(1L).build();
            Playlist playlist = Playlist.builder().playlistId(1L).user(user).build();
            Music music = Music.builder()
                    .musicId(1L)
                    .musicTitle("Test Music")
                    .musicUrl("http://test.com/music.mp3")
                    .playlist(playlist)
                    .build();
            Page<Music> musicPage = new PageImpl<>(java.util.Arrays.asList(music));

            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            given(playlistService.getPlayListById(1L)).willReturn(playlist);
            given(musicService.getAllMusicByPlaylist(anyLong(), org.mockito.ArgumentMatchers.<String>any(), any(Pageable.class))).willReturn(musicPage);

            // when & then
            mockMvc.perform(get("/musics/playlist/1/user/1").param("search", ""))
                    .andExpect(status().isOk());

            verify(musicService).getAllMusicByPlaylist(anyLong(), org.mockito.ArgumentMatchers.<String>any(), any(Pageable.class));
        }

        @Test
        @DisplayName("음악을 삭제한다")
        void testDeleteMusic() throws Exception {
            // when & then
            User user = User.builder().id(1L).build();
            given(userService.CheckUserAndGetUser(1L)).willReturn(user);
            mockMvc.perform(delete("/musics/music/1/user/1"))
                    .andExpect(status().isNoContent());

            verify(musicService).deleteMusic(org.mockito.ArgumentMatchers.eq(1L), any(User.class));
        }
    }
} 