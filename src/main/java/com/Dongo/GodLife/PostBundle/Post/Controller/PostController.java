package com.Dongo.GodLife.PostBundle.Post.Controller;

import com.Dongo.GodLife.PostBundle.Post.Dto.PageableRequest;
import com.Dongo.GodLife.PostBundle.Post.Dto.PostLikeResponse;
import com.Dongo.GodLife.PostBundle.Post.Dto.PostRequest;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.PostBundle.Post.Service.PostLikeService;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final PostLikeService postLikeService;
    private final UserService userService;
    
    @PostMapping("/user/{user_id}")
    public ResponseEntity<Post> createPost(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PostRequest postRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Post createdPost = postService.createPost(postRequest, user);
        return ResponseEntity.ok(createdPost);
    }
    
    // 공유된 포스트 조회 (아무나 볼 수 있음)
    @GetMapping("/shared/{post_id}")
    public ResponseEntity<Post> getSharedPostById(@PathVariable(name = "post_id") Long postId) {
        Post post = postService.findSharedById(postId);
        return ResponseEntity.ok(post);
    }
    
    // 나의 포스트 조회 (나만 볼 수 있음)
    @GetMapping("/my/{post_id}/user/{user_id}")
    public ResponseEntity<Post> getMyPostById(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        Post post = postService.findMyPostById(postId, userId);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Post>> getPostsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "category", required = false) String category,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Post> posts;
        
        if (category != null && !category.trim().isEmpty()) {
            PostType type = PostType.valueOf(category.toUpperCase());
            posts = postService.getMyPostsByType(user, type, pageable);
        } else {
            posts = postService.getMyPostsByType(user, null, pageable);
        }
        
        return ResponseEntity.ok(posts);
    }
    

    
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Post>> getPostsByCategory(
            @PathVariable String category,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        PostType type = PostType.valueOf(category.toUpperCase());
        Page<Post> posts;
        
        if (search != null && !search.trim().isEmpty()) {
            posts = postService.getPostsByTypeAndSearch(type, search, pageable);
        } else {
            posts = postService.getPostsByTypeAndSearch(type,null, pageable);
        }
        
        return ResponseEntity.ok(posts);
    }
    

    
    @GetMapping("/best")
    public ResponseEntity<Page<Post>> getBestPosts(
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "likeCount", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Post> posts = postService.getBestPosts(pageable);
        return ResponseEntity.ok(posts);
    }
    

    
    @GetMapping("/ads/user/{user_id}")
    public ResponseEntity<Page<Post>> getAdPostsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Post> posts = postService.getAdPostsByUser(user, pageable);
        return ResponseEntity.ok(posts);
    }
    

    
    @GetMapping("/ads")
    public ResponseEntity<Page<Post>> getAdPosts(
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Post> posts = postService.getAdPosts(pageable);
        return ResponseEntity.ok(posts);
    }
 
    
    @GetMapping("/shared/user/{user_id}")
    public ResponseEntity<Page<Post>> getSharedPostsByUser(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Post> posts = postService.getSharedPostsByUser(user, pageable);
        return ResponseEntity.ok(posts);
    }
    
    
    @GetMapping("/shared")
    public ResponseEntity<Page<Post>> getSharedPosts(
            @RequestParam(name = "shared", required = false, defaultValue = "true") boolean shared,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Post> posts = postService.getSharedPosts(pageable);
        return ResponseEntity.ok(posts);
    }
    
    
    @PutMapping("/{post_id}/user/{user_id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PostRequest postRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Post updatedPost = postService.updatePost(postId, postRequest, user);
        return ResponseEntity.ok(updatedPost);
    }
    
    @PutMapping("/ads/{ad_id}/toggle")
    public ResponseEntity<Post> toggleAdStatus(
            @PathVariable(name = "ad_id") Long adId,
            @RequestParam(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Post updatedPost = postService.toggleAdStatus(adId, user);
        return ResponseEntity.ok(updatedPost);
    }
    
    @DeleteMapping("/{post_id}/user/{user_id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        postService.deletePost(postId, user);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== 좋아요 관련 API ====================
    
    @PostMapping("/{post_id}/like/user/{user_id}")
    public ResponseEntity<PostLikeResponse> likePost(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        PostLikeResponse response = postLikeService.toggleLike(postId, user);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{post_id}/like/user/{user_id}")
    public ResponseEntity<Map<String, Object>> checkLikeStatus(
            @PathVariable(name = "post_id") Long postId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        boolean isLiked = postLikeService.isLikedByUser(postId, user);
        return ResponseEntity.ok(Map.of(
            "postId", postId,
            "userId", userId,
            "isLiked", isLiked
        ));
    }
} 