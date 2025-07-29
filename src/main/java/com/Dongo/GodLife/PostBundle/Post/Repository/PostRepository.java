package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 나의 포스트 찾기 (내가 쓴 게시물)
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.id = :postId")
    Post findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    
    // 공유된 포스트 찾기 (공유된 게시물)
    @Query("SELECT p FROM Post p WHERE p.id = :postId AND p.isShared = true")
    Post findByIsSharedTrueAndId(@Param("postId") Long postId);
    
    Page<Post> findByUser(User user, Pageable pageable);
    
    Page<Post> findByUserAndType(User user, PostType type, Pageable pageable);
    
    Page<Post> findByIsSharedTrue(Pageable pageable);
    
    Page<Post> findByIsAdTrue(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.isShared = true")
    Page<Post> findSharedPostsByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.isAd = true")
    Page<Post> findAdPostsByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:search% OR p.content LIKE %:search%")
    Page<Post> findBySearch(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE (:type IS NULL OR p.type = :type) AND (p.title LIKE %:search% OR p.content LIKE %:search%)")
    Page<Post> findByTypeAndSearch(@Param("type") PostType type, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE (:type IS NULL OR p.type = :type) AND (p.title LIKE %:search% OR p.content LIKE %:search%) AND p.isShared = true")
    Page<Post> findByTypeAndSearchAndIsSharedTrue(@Param("type") PostType type, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.isAd = true AND p.isShared = true")
    Page<Post> findByIsAdTrueAndIsSharedTrue(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.isAd = true AND p.isShared = true")
    Page<Post> findAdPostsByUserAndIsSharedTrue(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:search% OR p.content LIKE %:search%) AND p.isShared = true")
    Page<Post> findBySearchAndIsSharedTrue(@Param("search") String search, Pageable pageable);
} 