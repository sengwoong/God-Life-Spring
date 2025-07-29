package com.Dongo.GodLife.User.Repository;

import com.Dongo.GodLife.User.Model.Follow;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.FollowPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FollowAdapterImpl implements FollowPersistenceAdapter {

    private final FollowRepository followRepository;

    @Override
    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }

    @Override
    public Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public long countByFollowingId(Long followingId) {
        return followRepository.countByFollowingId(followingId);
    }

    @Override
    public long countByFollowerId(Long followerId) {
        return followRepository.countByFollowerId(followerId);
    }

    @Override
    public Page<Follow> findByFollowingId(Long followingId, Pageable pageable) {
        return followRepository.findByFollowingId(followingId, pageable);
    }

    @Override
    public Page<Follow> findByFollowerId(Long followerId, Pageable pageable) {
        return followRepository.findByFollowerId(followerId, pageable);
    }

    @Override
    public Page<User> findFollowersByUserId(Long followingId, Pageable pageable) {
        return followRepository.findFollowersByUserId(followingId, pageable);
    }

    @Override
    public Page<User> findFollowingByUserId(Long followerId, Pageable pageable) {
        return followRepository.findFollowingByUserId(followerId, pageable);
    }

    @Override
    public void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }
} 