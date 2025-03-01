package com.Dongo.GodLife.PostBundle.CommentLike.Controller;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.CommentLike.Model.CommentLike;
import com.Dongo.GodLife.PostBundle.CommentLike.Service.CommentLikeService;
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
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentLikeController {
    
    private final CommentLikeService commentLikeService;
    
    @PostMapping("/{comment_id}/likes")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable(name = "comment_id") Long commentId,
            @RequestParam(name = "user_id") Long userId) {
        
        CommentLike commentLike = commentLikeService.addLike(commentId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "댓글에 좋아요를 추가했습니다.");
        response.put("data", commentLike);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{comment_id}/likes")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable(name = "comment_id") Long commentId,
            @RequestParam(name = "user_id") Long userId) {
        
        commentLikeService.removeLike(commentId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "댓글 좋아요를 취소했습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{comment_id}/likes/check")
    public ResponseEntity<Map<String, Object>> checkLikeStatus(
            @PathVariable(name = "comment_id") Long commentId,
            @RequestParam(name = "user_id") Long userId) {
        
        boolean isLiked = commentLikeService.checkLikeStatus(commentId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("isLiked", isLiked);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/likes/user/{user_id}")
    public ResponseEntity<Page<Comment>> getLikedCommentsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> likedComments = commentLikeService.getLikedCommentsByUser(userId, pageable);
        
        return ResponseEntity.ok(likedComments);
    }
} 