package com.Dongo.GodLife.User.Repository;

import com.Dongo.GodLife.User.Model.Follow;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FollowAdapterImplTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowAdapterImpl followAdapter;

    @Test
    @DisplayName("save: Repository.save 위임")
    void save_delegates() {
        Follow f = Follow.builder().id(1L).build();
        given(followRepository.save(f)).willReturn(f);

        Follow result = followAdapter.save(f);
        assertEquals(1L, result.getId());
        verify(followRepository).save(f);
    }

    @Test
    @DisplayName("findByFollowerIdAndFollowingId: Optional 반환")
    void findByFollowerIdAndFollowingId() {
        given(followRepository.findByFollowerIdAndFollowingId(1L, 2L))
                .willReturn(Optional.of(Follow.builder().id(5L).build()));

        Optional<Follow> result = followAdapter.findByFollowerIdAndFollowingId(1L, 2L);
        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
        verify(followRepository).findByFollowerIdAndFollowingId(1L, 2L);
    }

    @Test
    @DisplayName("existsByFollowerIdAndFollowingId: 존재 여부 반환")
    void existsByFollowerIdAndFollowingId() {
        given(followRepository.existsByFollowerIdAndFollowingId(1L, 2L)).willReturn(true);

        boolean exists = followAdapter.existsByFollowerIdAndFollowingId(1L, 2L);
        assertTrue(exists);
        verify(followRepository).existsByFollowerIdAndFollowingId(1L, 2L);
    }

    @Test
    @DisplayName("countByFollowingId/countByFollowerId 위임")
    void countDelegations() {
        given(followRepository.countByFollowingId(2L)).willReturn(3L);
        given(followRepository.countByFollowerId(1L)).willReturn(4L);

        assertEquals(3L, followAdapter.countByFollowingId(2L));
        assertEquals(4L, followAdapter.countByFollowerId(1L));
    }

    @Test
    @DisplayName("페이징 조회 위임: findByFollowingId/findByFollowerId")
    void pagingDelegations() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Follow> page = new PageImpl<>(List.of(Follow.builder().id(1L).build()));
        given(followRepository.findByFollowingId(2L, pageable)).willReturn(page);
        given(followRepository.findByFollowerId(1L, pageable)).willReturn(page);

        assertEquals(1, followAdapter.findByFollowingId(2L, pageable).getTotalElements());
        assertEquals(1, followAdapter.findByFollowerId(1L, pageable).getTotalElements());
    }

    @Test
    @DisplayName("사용자 목록 조회 위임: findFollowersByUserId/findFollowingByUserId")
    void userPages() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> users = new PageImpl<>(List.of(User.builder().id(7L).build()));
        given(followRepository.findFollowersByUserId(2L, pageable)).willReturn(users);
        given(followRepository.findFollowingByUserId(1L, pageable)).willReturn(users);

        assertEquals(1, followAdapter.findFollowersByUserId(2L, pageable).getTotalElements());
        assertEquals(1, followAdapter.findFollowingByUserId(1L, pageable).getTotalElements());
    }

    @Test
    @DisplayName("deleteByFollowerIdAndFollowingId 위임")
    void deleteDelegation() {
        followAdapter.deleteByFollowerIdAndFollowingId(1L, 2L);
        verify(followRepository).deleteByFollowerIdAndFollowingId(1L, 2L);
    }
}


