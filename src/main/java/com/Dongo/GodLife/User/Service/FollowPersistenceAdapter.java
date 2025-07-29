package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Model.Follow;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FollowPersistenceAdapter {

    Follow save(Follow follow);
    
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    long countByFollowingId(Long followingId);
    
    long countByFollowerId(Long followerId);
    
    Page<Follow> findByFollowingId(Long followingId, Pageable pageable);
    
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);
    
    Page<User> findFollowersByUserId(Long followingId, Pageable pageable);
    
    Page<User> findFollowingByUserId(Long followerId, Pageable pageable);
    
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
} 