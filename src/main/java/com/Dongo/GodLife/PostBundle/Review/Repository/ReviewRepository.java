package com.Dongo.GodLife.PostBundle.Review.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Review.Model.Review;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByPost(Post post, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
    Optional<Review> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.post = :post")
    Double getAverageRatingByPost(Post post);
    
    // 최신 리뷰 조회 (최근 작성된 순으로)
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 특정 평점 이상의 리뷰 조회
    Page<Review> findByRatingGreaterThanEqual(Double rating, Pageable pageable);
    
    // 특정 사용자가 작성한 리뷰 중 평점이 높은 순으로 조회
    Page<Review> findByUserOrderByRatingDesc(User user, Pageable pageable);
    
    // 특정 게시물의 리뷰 수 조회
    Long countByPost(Post post);
    
    // 특정 사용자의 평균 평점 조회
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.user = :user")
    Double getAverageRatingByUser(User user);
    
    // 특정 게시물의 최근 리뷰 n개 조회
    List<Review> findTop5ByPostOrderByCreatedAtDesc(Post post);
} 