package com.Dongo.GodLife.PostBundle.Review.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Review.Model.Review;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByPost(Post post, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
    Optional<Review> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.post = :post")
    Double getAverageRatingByPost(Post post);
} 