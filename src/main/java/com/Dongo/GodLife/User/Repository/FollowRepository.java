package com.Dongo.GodLife.User.Repository;

import com.Dongo.GodLife.User.Model.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    // 특정 사용자가 다른 사용자를 팔로우하고 있는지 확인
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    // 팔로우 관계 존재 여부 확인
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    // 특정 사용자의 팔로워 수 조회
    long countByFollowingId(Long followingId);
    
    // 특정 사용자가 팔로우하는 수 조회
    long countByFollowerId(Long followerId);
    
    // 특정 사용자의 팔로워 목록 조회
    Page<Follow> findByFollowingId(Long followingId, Pageable pageable);
    
    // 특정 사용자가 팔로우하는 목록 조회
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);
    
    // 특정 사용자의 팔로워 목록 (User 정보 포함)
    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :followingId")
    Page<com.Dongo.GodLife.User.Model.User> findFollowersByUserId(@Param("followingId") Long followingId, Pageable pageable);
    
    // 특정 사용자가 팔로우하는 목록 (User 정보 포함)
    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :followerId")
    Page<com.Dongo.GodLife.User.Model.User> findFollowingByUserId(@Param("followerId") Long followerId, Pageable pageable);
    
    // 팔로우 관계 삭제
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void deleteByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
} 