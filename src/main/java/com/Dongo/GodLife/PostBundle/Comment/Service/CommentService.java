package com.Dongo.GodLife.PostBundle.Comment.Service;

import com.Dongo.GodLife.PostBundle.Comment.Dto.CommentRequest;
import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
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
public class CommentService {
    
    private final CommentPersistenceAdapter commentRepository;
    private final PostService postService;
    private final UserService userService;
    
    @Transactional
    public Comment createComment(Long postId, Long userId, CommentRequest commentRequest) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .userNickName(user.getNickName())
                .content(commentRequest.getContent())
                .likeCount(0)
                .build();
        
        return commentRepository.save(comment);
    }
    
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        Post post = postService.getById(postId);
        return commentRepository.findByPost(post, pageable);
    }
    
    @Transactional
    public Comment updateComment(Long commentId, Long userId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        
        // 댓글 작성자 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신의 댓글만 수정할 수 있습니다.");
        }
        
        comment.setContent(commentRequest.getContent());
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        
        // 댓글 작성자 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("자신의 댓글만 삭제할 수 있습니다.");
        }
        
        commentRepository.deleteById(commentId);
    }
    
    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }
    
    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
} 