package com.Dongo.GodLife.User;

import com.Dongo.GodLife.User.Dto.UpdateUserRequest;
import com.Dongo.GodLife.User.Exception.UserNotFoundException;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @InjectMocks
    UserService sut;

    @Spy
    UserPersistenceAdapterStub userRepository;

    @Nested
    class CreateUser {
        @Test
        @DisplayName("새로운 사용자를 생성하고 저장한다")
        void testCreateUser() {
            // given
            String email = "test@test.com";
            String password = "password";
            String nickName = "nickname";

            // when
            User result = sut.createUser(email, password, nickName);

            // then
            assertNotNull(result);
            assertEquals(email, result.getEmail());
            assertEquals(password, result.getPassword());
            assertEquals(nickName, result.getNickName());
        }
    }

    @Nested
    class GetUser {
        @Test
        @DisplayName("존재하는 사용자 ID로 조회하면 사용자를 반환한다")
        void testGetExistingUser() {
            // given
            User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickName("nickname")
                .build();
            userRepository.save(user);

            // when
            User result = sut.CheckUserAndGetUser(1L);

            // then
            assertNotNull(result);
            assertEquals("test@test.com", result.getEmail());
            assertEquals("nickname", result.getNickName());
        }

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 조회하면 예외가 발생한다")
        void testGetNonExistingUser() {
            // when & then
            assertThrows(UserNotFoundException.class,
                () -> sut.CheckUserAndGetUser(999L));
        }
    }

    @Nested
    class UpdateUser {
        @Test
        @DisplayName("사용자 정보를 업데이트한다")
        void testUpdateUser() {
            // given
            User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickName("nickname")
                .build();
            userRepository.save(user);

            UpdateUserRequest request = new UpdateUserRequest(
                User.builder()
                    .email("updated@test.com")
                    .nickName("updatedNick")
                    .phoneNumber("010-1234-5678")
                    .address("Seoul")
                    .build()
            );

            // when
            UpdateUserRequest result = sut.updateUser(1L, request);

            // then
            assertNotNull(result);
            assertEquals("updated@test.com", result.getEmail());
            assertEquals("updatedNick", result.getNickName());
            assertEquals("010-1234-5678", result.getPhoneNumber());
            assertEquals("Seoul", result.getAddress());
        }
    }

    @Nested
    class DeleteUser {
        @Test
        @DisplayName("사용자를 삭제한다")
        void testDeleteUser() {
            // given
            User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickName("nickname")
                .build();
            userRepository.save(user);

            // when & then
            assertDoesNotThrow(() -> sut.deleteUser(1L));
            
            // 삭제 확인
            assertThrows(UserNotFoundException.class,
                () -> sut.CheckUserAndGetUser(1L));
        }
    }
} 