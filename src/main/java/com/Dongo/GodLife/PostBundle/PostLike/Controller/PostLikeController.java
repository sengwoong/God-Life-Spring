package com.Dongo.GodLife.PostBundle.PostLike.Controller;

import com.Dongo.GodLife.PostBundle.PostLike.Model.PostLike;
import com.Dongo.GodLife.PostBundle.PostLike.Service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostLikeController {
    
    private final PostLikeService postLikeService;
    
    @PostMapping("/{post_id}/likes")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "user_id") Long userId) {
        
        PostLike postLike = postLikeService.addLike(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "게시물에 좋아요를 추가했습니다.");
        response.put("data", postLike);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{post_id}/likes")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "user_id") Long userId) {
        
        postLikeService.removeLike(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "게시물 좋아요를 취소했습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{post_id}/likes/check")
    public ResponseEntity<Map<String, Object>> checkLikeStatus(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "user_id") Long userId) {
        
        boolean isLiked = postLikeService.checkLikeStatus(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("isLiked", isLiked);
        
        return ResponseEntity.ok(response);
    }
} 