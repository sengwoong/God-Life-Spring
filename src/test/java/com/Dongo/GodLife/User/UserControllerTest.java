package com.Dongo.GodLife.User;

import com.Dongo.GodLife.User.Controller.UserController;
import com.Dongo.GodLife.User.Dto.FollowResponse;
import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserCreateRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.FollowService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private FollowService followService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        objectMapper = mapper;
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new org.springframework.data.web.PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Nested
    class UserManagement {
        @Test
        @DisplayName("새로운 사용자를 생성한다")
        void testCreateUser() throws Exception {
            // given
            UserCreateRequest request = new UserCreateRequest("test@test.com", "Passw0rd!", "nicknm");
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .nickName("nickname")
                    .createdAt(LocalDateTime.now())
                    .build();

            given(userService.createUser(anyString(), anyString(), anyString())).willReturn(user);

            // when & then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(userService).createUser(org.mockito.ArgumentMatchers.eq("test@test.com"), org.mockito.ArgumentMatchers.eq("Passw0rd!"), org.mockito.ArgumentMatchers.eq("nicknm"));
        }

        @Test
        @DisplayName("사용자 ID로 사용자 정보를 조회한다")
        void testGetUserById() throws Exception {
            // given
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .nickName("nickname")
                    .createdAt(LocalDateTime.now())
                    .build();
            UserResponse response = new UserResponse(user);

            given(userService.getUserByUserDetail(1L)).willReturn(response);

            // when & then
            mockMvc.perform(get("/users/user/1"))
                    .andExpect(status().isOk());

            verify(userService).getUserByUserDetail(1L);
        }

        @Test
        @DisplayName("사용자 정보를 업데이트한다")
        void testUpdateUser() throws Exception {
            // given
            UpdateUserRequest request = new UpdateUserRequest(User.builder()
                    .email("updated@test.com")
                    .nickName("updNick")
                    .phoneNumber("010-1234-5678")
                    .address("Seoul")
                    .build());
            User updatedUser = User.builder()
                    .id(1L)
                    .email("updated@test.com")
                    .nickName("updatedNick")
                    .phoneNumber("010-1234-5678")
                    .address("Seoul")
                    .build();
            UpdateUserRequest response = new UpdateUserRequest(updatedUser);

            given(userService.updateUser(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(UpdateUserRequest.class))).willReturn(response);

            // when & then
            mockMvc.perform(put("/users/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(userService).updateUser(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(UpdateUserRequest.class));
        }

        @Test
        @DisplayName("사용자를 삭제한다")
        void testDeleteUser() throws Exception {
            // when & then
            mockMvc.perform(delete("/users/user/1"))
                    .andExpect(status().isNoContent());

            verify(userService).deleteUser(1L);
        }
    }

    @Nested
    class FollowManagement {
        @Test
        @DisplayName("팔로우를 토글한다")
        void testToggleFollow() throws Exception {
            // given
            FollowResponse response = FollowResponse.builder()
                    .followerId(1L)
                    .followingId(2L)
                    .isFollowing(true)
                    .build();
            given(followService.toggleFollow(1L, 2L)).willReturn(response);

            // when & then
            mockMvc.perform(post("/users/1/follow/2"))
                    .andExpect(status().isOk());

            verify(followService).toggleFollow(1L, 2L);
        }

        @Test
        @DisplayName("팔로우 상태를 확인한다")
        void testCheckFollowStatus() throws Exception {
            // given
            given(followService.isFollowing(1L, 2L)).willReturn(true);

            // when & then
            mockMvc.perform(get("/users/1/follow/2"))
                    .andExpect(status().isOk());

            verify(followService).isFollowing(1L, 2L);
        }

        @Test
        @DisplayName("팔로워 목록을 조회한다")
        void testGetFollowers() throws Exception {
            // given
            User follower = new User();
            follower.setId(2L);
            follower.setNickName("follower");
            follower.setEmail("follower@test.com");
            follower.setPassword("password");
            follower.setLevel(1);
            follower.setFollowers(0);
            follower.setFollowing(0);
            follower.setSales(0);
            follower.setCreatedAt(LocalDateTime.now());
            
            // 실제 Pageable 객체 생성
            Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
            Page<User> followerPage = new PageImpl<>(List.of(follower), pageable, 1);
            given(followService.getFollowers(eq(1L), any(Pageable.class))).willReturn(followerPage);

            // when & then
            mockMvc.perform(get("/users/1/followers")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk());

            verify(followService).getFollowers(eq(1L), any(Pageable.class));
        }

        @Test
        @DisplayName("팔로잉 목록을 조회한다")
        void testGetFollowing() throws Exception {
            // given
            User following = new User();
            following.setId(3L);
            following.setNickName("following");
            following.setEmail("following@test.com");
            following.setPassword("password");
            following.setLevel(1);
            following.setFollowers(0);
            following.setFollowing(0);
            following.setSales(0);
            following.setCreatedAt(LocalDateTime.now());
            
            // 실제 Pageable 객체 생성
            Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
            Page<User> followingPage = new PageImpl<>(List.of(following), pageable, 1);
            given(followService.getFollowing(eq(1L), any(Pageable.class))).willReturn(followingPage);

            // when & then
            mockMvc.perform(get("/users/1/following")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk());

            verify(followService).getFollowing(eq(1L), any(Pageable.class));
        }
    }
} 