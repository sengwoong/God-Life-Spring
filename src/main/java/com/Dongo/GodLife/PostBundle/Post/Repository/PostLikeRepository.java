package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    // 특정 포스트와 사용자의 좋아요 조회
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    
    // 특정 포스트의 좋아요 존재 여부 확인
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    
    Optional<PostLike> findByPostIdAndUserIdAndIsSharedTrue(Long postId, Long userId);
    
    boolean existsByPostIdAndUserIdAndIsSharedTrue(Long postId, Long userId);
    
    Optional<PostLike> findByPostIdAndUserIdAndIsAdTrue(Long postId, Long userId);
    
    boolean existsByPostIdAndUserIdAndIsAdTrue(Long postId, Long userId);
    
    // 특정 포스트의 좋아요 개수 조회
    long countByPostId(Long postId);
    
    // 특정 사용자가 좋아요한 포스트 목록 조회
    List<PostLike> findByUserId(Long userId);
    
    // 특정 포스트의 모든 좋아요 조회
    List<PostLike> findByPostId(Long postId);
    
    // 특정 포스트와 사용자의 좋아요 삭제
    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post.id = :postId AND pl.user.id = :userId")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
    
    // 특정 포스트의 모든 좋아요 삭제
    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
} 