package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Dto.FollowResponse;
import com.Dongo.GodLife.User.Model.Follow;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    
    private final FollowPersistenceAdapter followPersistenceAdapter;
    private final UserService userService;
    
    /**
     * 팔로우 토글
     */
    @Transactional
    public FollowResponse toggleFollow(Long followerId, Long followingId) {
        User follower = userService.CheckUserAndGetUser(followerId);
        User following = userService.CheckUserAndGetUser(followingId);
        
        // 자기 자신을 팔로우할 수 없음
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        
        // 이미 팔로우하고 있는지 확인
        boolean isFollowing = followPersistenceAdapter.existsByFollowerIdAndFollowingId(followerId, followingId);
        
        if (isFollowing) {
            // 언팔로우
            followPersistenceAdapter.deleteByFollowerIdAndFollowingId(followerId, followingId);
            
            // 팔로워/팔로잉 수 업데이트
            follower.setFollowing(follower.getFollowing() - 1);
            following.setFollowers(following.getFollowers() - 1);
            userService.saveUser(follower);
            userService.saveUser(following);
            
            return FollowResponse.builder()
                    .followerId(followerId)
                    .followingId(followingId)
                    .isFollowing(false)
                    .followerCount(following.getFollowers())
                    .followingCount(follower.getFollowing())
                    .build();
        } else {
            // 팔로우
            Follow follow = Follow.builder()
                    .follower(follower)
                    .following(following)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            followPersistenceAdapter.save(follow);
            
            // 팔로워/팔로잉 수 업데이트
            follower.setFollowing(follower.getFollowing() + 1);
            following.setFollowers(following.getFollowers() + 1);
            userService.saveUser(follower);
            userService.saveUser(following);
            
            return FollowResponse.builder()
                    .followerId(followerId)
                    .followingId(followingId)
                    .isFollowing(true)
                    .createdAt(follow.getCreatedAt())
                    .followerCount(following.getFollowers())
                    .followingCount(follower.getFollowing())
                    .build();
        }
    }
    
    /**
     * 팔로우 상태 확인
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return followPersistenceAdapter.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
    

    public long getFollowerCount(Long userId) {
        return followPersistenceAdapter.countByFollowingId(userId);
    }
    

    public long getFollowingCount(Long userId) {
        return followPersistenceAdapter.countByFollowerId(userId);
    }
    

    public Page<User> getFollowers(Long userId, Pageable pageable) {
        return followPersistenceAdapter.findFollowersByUserId(userId, pageable);
    }
    

    public Page<User> getFollowing(Long userId, Pageable pageable) {
        return followPersistenceAdapter.findFollowingByUserId(userId, pageable);
    }
} 