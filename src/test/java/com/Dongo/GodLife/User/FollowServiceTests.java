package com.Dongo.GodLife.User;

import com.Dongo.GodLife.User.Dto.FollowResponse;
import com.Dongo.GodLife.User.Model.Follow;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.FollowService;
import com.Dongo.GodLife.User.Service.FollowPersistenceAdapter;
import com.Dongo.GodLife.User.Service.UserService;
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
class FollowServiceTests {

    @Mock
    private FollowPersistenceAdapter followPersistenceAdapter;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService followService;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        follower = User.builder()
                .id(1L)
                .email("follower@test.com")
                .nickName("follower")
                .following(0)
                .build();

        following = User.builder()
                .id(2L)
                .email("following@test.com")
                .nickName("following")
                .followers(0)
                .build();
    }

    @Nested
    class ToggleFollow {
        @Test
        @DisplayName("팔로우를 추가한다")
        void testAddFollow() {
            // given
            given(userService.CheckUserAndGetUser(1L)).willReturn(follower);
            given(userService.CheckUserAndGetUser(2L)).willReturn(following);
            given(followPersistenceAdapter.existsByFollowerIdAndFollowingId(1L, 2L)).willReturn(false);
            given(userService.saveUser(any(User.class))).willReturn(follower, following);

            // when
            FollowResponse result = followService.toggleFollow(1L, 2L);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getFollowerId());
            assertEquals(2L, result.getFollowingId());
            assertTrue(result.isFollowing());
            verify(followPersistenceAdapter).save(any(Follow.class));
        }

        @Test
        @DisplayName("팔로우를 제거한다")
        void testRemoveFollow() {
            // given
            given(userService.CheckUserAndGetUser(1L)).willReturn(follower);
            given(userService.CheckUserAndGetUser(2L)).willReturn(following);
            given(followPersistenceAdapter.existsByFollowerIdAndFollowingId(1L, 2L)).willReturn(true);
            given(userService.saveUser(any(User.class))).willReturn(follower, following);

            // when
            FollowResponse result = followService.toggleFollow(1L, 2L);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getFollowerId());
            assertEquals(2L, result.getFollowingId());
            assertFalse(result.isFollowing());
            verify(followPersistenceAdapter).deleteByFollowerIdAndFollowingId(1L, 2L);
        }

        @Test
        @DisplayName("자기 자신을 팔로우하려고 하면 예외가 발생한다")
        void testFollowSelfThrowsException() {
            // when & then
            assertThrows(IllegalArgumentException.class, () -> followService.toggleFollow(1L, 1L));
            verify(followPersistenceAdapter, never()).save(any(Follow.class));
        }
    }

    @Nested
    class FollowStatus {
        @Test
        @DisplayName("팔로우 상태를 확인한다")
        void testIsFollowing() {
            // given
            given(followPersistenceAdapter.existsByFollowerIdAndFollowingId(1L, 2L)).willReturn(true);

            // when
            boolean result = followService.isFollowing(1L, 2L);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("팔로우하지 않는 상태를 확인한다")
        void testIsNotFollowing() {
            // given
            given(followPersistenceAdapter.existsByFollowerIdAndFollowingId(1L, 2L)).willReturn(false);

            // when
            boolean result = followService.isFollowing(1L, 2L);

            // then
            assertFalse(result);
        }
    }

    @Nested
    class FollowCounts {
        @Test
        @DisplayName("팔로워 수를 조회한다")
        void testGetFollowerCount() {
            // given
            given(followPersistenceAdapter.countByFollowingId(2L)).willReturn(5L);

            // when
            long result = followService.getFollowerCount(2L);

            // then
            assertEquals(5L, result);
        }

        @Test
        @DisplayName("팔로잉 수를 조회한다")
        void testGetFollowingCount() {
            // given
            given(followPersistenceAdapter.countByFollowerId(1L)).willReturn(3L);

            // when
            long result = followService.getFollowingCount(1L);

            // then
            assertEquals(3L, result);
        }
    }

    @Nested
    class FollowLists {
        @Test
        @DisplayName("팔로워 목록을 조회한다")
        void testGetFollowers() {
            // given
            User follower1 = User.builder().id(3L).nickName("follower1").build();
            User follower2 = User.builder().id(4L).nickName("follower2").build();
            Page<User> followersPage = new PageImpl<>(List.of(follower1, follower2));
            given(followPersistenceAdapter.findFollowersByUserId(org.mockito.ArgumentMatchers.eq(2L), any(Pageable.class))).willReturn(followersPage);

            // when
            Page<User> result = followService.getFollowers(2L, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("follower1", result.getContent().get(0).getNickName());
            assertEquals("follower2", result.getContent().get(1).getNickName());
        }

        @Test
        @DisplayName("팔로잉 목록을 조회한다")
        void testGetFollowing() {
            // given
            User following1 = User.builder().id(5L).nickName("following1").build();
            User following2 = User.builder().id(6L).nickName("following2").build();
            Page<User> followingPage = new PageImpl<>(List.of(following1, following2));
            given(followPersistenceAdapter.findFollowingByUserId(org.mockito.ArgumentMatchers.eq(1L), any(Pageable.class))).willReturn(followingPage);

            // when
            Page<User> result = followService.getFollowing(1L, Pageable.unpaged());

            // then
            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("following1", result.getContent().get(0).getNickName());
            assertEquals("following2", result.getContent().get(1).getNickName());
        }
    }
}
