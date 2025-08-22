package com.Dongo.GodLife.User.Repository;

import com.Dongo.GodLife.User.Exception.UserNotFoundException;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAdapterImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdapterImpl userAdapter;

    @Test
    @DisplayName("save(User): 전달된 필드가 복사되어 저장된다")
    void save_copiesFieldsAndSaves() {
        User request = User.builder()
                .email("test@test.com")
                .password("password")
                .nickName("nick")
                .createdAt(LocalDateTime.now())
                .build();

        User saved = User.builder()
                .id(1L)
                .email(request.getEmail())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .createdAt(request.getCreatedAt())
                .build();

        given(userRepository.save(any(User.class))).willReturn(saved);

        User result = userAdapter.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getPassword(), result.getPassword());
        assertEquals(request.getNickName(), result.getNickName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("findById: Repository 위임")
    void findById_delegates() {
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));

        Optional<User> result = userAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("delete: 존재하지 않으면 예외")
    void delete_throwsWhenNotFound() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userAdapter.delete(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("delete: 존재하면 삭제 호출")
    void delete_deletesWhenFound() {
        User u = User.builder().id(2L).build();
        given(userRepository.findById(2L)).willReturn(Optional.of(u));

        userAdapter.delete(2L);

        verify(userRepository).delete(eq(u));
    }

    @Test
    @DisplayName("saveUser: Repository.save 위임")
    void saveUser_delegates() {
        User u = User.builder().email("a@a.com").build();
        given(userRepository.save(u)).willReturn(User.builder().id(10L).email("a@a.com").build());

        User result = userAdapter.saveUser(u);

        assertEquals(10L, result.getId());
        verify(userRepository).save(u);
    }

    @Test
    @DisplayName("findByEmail: Repository 위임")
    void findByEmail_delegates() {
        given(userRepository.findByEmail("me@me.com"))
                .willReturn(Optional.of(User.builder().id(3L).email("me@me.com").build()));

        Optional<User> result = userAdapter.findByEmail("me@me.com");

        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getId());
        verify(userRepository).findByEmail("me@me.com");
    }
}


