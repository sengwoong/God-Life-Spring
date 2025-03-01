package com.Dongo.GodLife.PostBundle.Post.Controller;

import com.Dongo.GodLife.PostBundle.Post.Dto.PostRequest;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
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
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final UserService userService;
    
    @PostMapping("/user/{user_id}")
    public ResponseEntity<Post> createPost(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody PostRequest postRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.createPost(postRequest, user);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Post>> getPostsByUserId(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        User user = userService.CheckUserAndGetUser(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postService.getAllPostsByUserId(user, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{post_id}")
    public ResponseEntity<Post> getPostById(
            @PathVariable(name = "post_id") Long postId) {
        Post post = postService.getById(postId);
        return ResponseEntity.ok(post);
    }
    
    @PutMapping("/{post_id}/user/{user_id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody PostRequest postRequest) {
        userService.CheckUserAndGetUser(userId);
        Post updatedPost = postService.updatePost(postId, userId, postRequest);
        return ResponseEntity.ok(updatedPost);
    }
    
    @DeleteMapping("/{post_id}/user/{user_id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        userService.CheckUserAndGetUser(userId);
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{post_id}/share")
    public ResponseEntity<Map<String, Object>> toggleSharePost(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "user_id") Long userId) {
        
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.toggleSharePost(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", post);
        response.put("message", post.isShared() ? 
                "게시물이 공유되었습니다." : "게시물 공유가 취소되었습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/shared")
    public ResponseEntity<Page<Post>> getSharedPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> sharedPosts = postService.getAllSharedPosts(pageable);
        return ResponseEntity.ok(sharedPosts);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<Post>> getPostsByType(
            @PathVariable(name = "type") Post.PostType type,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postService.getPostsByType(type, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/advertisements")
    public ResponseEntity<Page<Post>> getAdvertisementPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> advertisementPosts = postService.getAdvertisementPosts(pageable);
        return ResponseEntity.ok(advertisementPosts);
    }
} 