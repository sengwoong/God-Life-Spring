package com.Dongo.GodLife.PostBundle.Review.Service;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
import com.Dongo.GodLife.PostBundle.Review.Dto.ReviewRequest;
import com.Dongo.GodLife.PostBundle.Review.Model.Review;
import com.Dongo.GodLife.PostBundle.Review.Repository.ReviewRepository;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final PostService postService;
    
    @Transactional
    public Review createReview(Long postId, Long userId, ReviewRequest reviewRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("이미 리뷰를 작성한 게시물입니다.");
        }
        
        Review review = Review.builder()
                .user(user)
                .post(post)
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .build();
        
        // 게시물의 평균 평점 업데이트
        updatePostAverageRating(post);
        
        return reviewRepository.save(review);
    }
    
    @Transactional
    public Review updateReview(Long reviewId, Long userId, ReviewRequest reviewRequest) {
        Review review = getReviewById(reviewId);
        
        // 리뷰 작성자 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰를 수정할 권한이 없습니다.");
        }
        
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
        
        // 게시물의 평균 평점 업데이트
        updatePostAverageRating(review.getPost());
        
        return reviewRepository.save(review);
    }
    
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        
        // 리뷰 작성자 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰를 삭제할 권한이 없습니다.");
        }
        
        Post post = review.getPost();
        reviewRepository.delete(review);
        
        // 게시물의 평균 평점 업데이트
        updatePostAverageRating(post);
    }
    
    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
    }
    
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByPost(Long postId, Pageable pageable) {
        Post post = postService.getById(postId);
        return reviewRepository.findByPost(post, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        return reviewRepository.findByUser(user, pageable);
    }
    
    @Transactional(readOnly = true)
    public Review getUserReviewForPost(Long postId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        return reviewRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물에 대한 리뷰를 찾을 수 없습니다."));
    }
    
    private void updatePostAverageRating(Post post) {
        Double averageRating = reviewRepository.getAverageRatingByPost(post);
        if (averageRating != null) {
            post.updateRating(averageRating);
            postService.save(post);
        } else {
            post.updateRating(0.0);
            postService.save(post);
        }
    }
} 