package com.Dongo.GodLife.User;

import com.Dongo.GodLife.User.Controller.UserController;
import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Dto.UserCreateRequest;
import com.Dongo.GodLife.User.Dto.UserResponse;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("사용자 관리")
    @Nested
    class UserManagement {

        @DisplayName("사용자 생성")
        @Nested
        class CreateUser {
            @Test
            @DisplayName("정상적인 요청일 경우, 200 status와 User를 반환한다")
            void createUser_Success() throws Exception {
                // given
                UserCreateRequest request = new UserCreateRequest("test@test.com", "password", "nickname");
                User user = User.builder()
                    .email("test@test.com")
                    .password("password")
                    .nickName("nickname")
                    .build();

                given(userService.createUser(any(), any(), any())).willReturn(user);

                // when & then
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@test.com"))
                    .andExpect(jsonPath("$.nickName").value("nickname"));

                verify(userService).createUser(any(), any(), any());
            }
        }

        @DisplayName("사용자 조회")
        @Nested
        class GetUser {
            @Test
            @DisplayName("사용자 ID로 조회 시 사용자 정보를 반환한다")
            void getUser_Success() throws Exception {
                // given
                UserResponse userResponse = new UserResponse(
                    User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickName("nickname")
                        .build()
                );

                given(userService.getUserByUserDetail(anyLong())).willReturn(userResponse);

                // when & then
                mockMvc.perform(get("/users/user/{user_id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@test.com"))
                    .andExpect(jsonPath("$.nickName").value("nickname"));

                verify(userService).getUserByUserDetail(anyLong());
            }
        }

        @DisplayName("사용자 수정")
        @Nested
        class UpdateUser {
            @Test
            @DisplayName("정상적인 요청일 경우, 수정된 사용자 정보를 반환한다")
            void updateUser_Success() throws Exception {
                // given
                UpdateUserRequest request = new UpdateUserRequest(
                    User.builder()
                        .email("updated@test.com")
                        .nickName("updatedNick")
                        .phoneNumber("010-1234-5678")
                        .address("Seoul")
                        .build()
                );

                given(userService.updateUser(anyLong(), any(UpdateUserRequest.class)))
                    .willReturn(request);

                // when & then
                mockMvc.perform(put("/users/user/{user_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("updated@test.com"))
                    .andExpect(jsonPath("$.nickName").value("updatedNick"));

                verify(userService).updateUser(anyLong(), any(UpdateUserRequest.class));
            }
        }

        @DisplayName("사용자 삭제")
        @Nested
        class DeleteUser {
            @Test
            @DisplayName("정상적인 요청일 경우, 204 status를 반환한다")
            void deleteUser_Success() throws Exception {
                // when & then
                mockMvc.perform(delete("/users/user/{user_id}", 1L))
                    .andExpect(status().isNoContent());

                verify(userService).deleteUser(anyLong());
            }
        }
    }
} 