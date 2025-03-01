package com.Dongo.GodLife.PostBundle.Review.Controller;

import com.Dongo.GodLife.PostBundle.Review.Dto.ReviewRequest;
import com.Dongo.GodLife.PostBundle.Review.Model.Review;
import com.Dongo.GodLife.PostBundle.Review.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping("/posts/{post_id}")
    public ResponseEntity<Map<String, Object>> createReview(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "user_id") Long userId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        
        Review review = reviewService.createReview(postId, userId, reviewRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "리뷰가 성공적으로 작성되었습니다.");
        response.put("data", review);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{review_id}")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable(name = "review_id") Long reviewId,
            @RequestParam(name = "user_id") Long userId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        
        Review review = reviewService.updateReview(reviewId, userId, reviewRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "리뷰가 성공적으로 수정되었습니다.");
        response.put("data", review);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{review_id}")
    public ResponseEntity<Map<String, Object>> deleteReview(
            @PathVariable(name = "review_id") Long reviewId,
            @RequestParam(name = "user_id") Long userId) {
        
        reviewService.deleteReview(reviewId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "리뷰가 성공적으로 삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<Page<Review>> getReviewsByPost(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews = reviewService.getReviewsByPost(postId, pageable);
        
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/users/{user_id}")
    public ResponseEntity<Page<Review>> getReviewsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews = reviewService.getReviewsByUser(userId, pageable);
        
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/posts/{post_id}/users/{user_id}")
    public ResponseEntity<Map<String, Object>> getUserReviewForPost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        
        Review review = reviewService.getUserReviewForPost(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", review);
        
        return ResponseEntity.ok(response);
    }
} 