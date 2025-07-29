package com.Dongo.GodLife.PostBundle.Comment.Controller;

import com.Dongo.GodLife.PostBundle.Comment.Dto.CommentRequest;
import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Comment.Service.CommentService;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    private final UserService userService;
    
    @PostMapping("/user/{user_id}")
    public ResponseEntity<Comment> createComment(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid CommentRequest commentRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Comment createdComment = commentService.createComment(commentRequest, user);
        return ResponseEntity.ok(createdComment);
    }
    
    @GetMapping("/{comment_id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable(name = "comment_id") Long commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/post/{post_id}")
    public ResponseEntity<Page<Comment>> getCommentsByPost(
            @PathVariable(name = "post_id") Long postId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<Comment>> getCommentsByUser(
            @PathVariable(name = "user_id") Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        Page<Comment> comments = commentService.getCommentsByUser(user, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/post/{post_id}/count")
    public ResponseEntity<Long> getCommentCountByPost(@PathVariable(name = "post_id") Long postId) {
        Long count = commentService.getCommentCountByPost(postId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{comment_id}/user/{user_id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable(name = "comment_id") Long commentId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid CommentRequest commentRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Comment updatedComment = commentService.updateComment(commentId, commentRequest, user);
        return ResponseEntity.ok(updatedComment);
    }
    
    @DeleteMapping("/{comment_id}/user/{user_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(name = "comment_id") Long commentId,
            @PathVariable(name = "user_id") Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }
} 