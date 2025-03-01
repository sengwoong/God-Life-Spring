package com.Dongo.GodLife.PostBundle.CommentLike.Service;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Comment.Service.CommentService;
import com.Dongo.GodLife.PostBundle.CommentLike.Model.CommentLike;
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
public class CommentLikeService {
    
    private final CommentLikePersistenceAdapter commentLikeRepository;
    private final CommentService commentService;
    private final UserService userService;
    
    @Transactional
    public CommentLike addLike(Long commentId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Comment comment = commentService.getCommentById(commentId);
        
        // 이미 좋아요를 눌렀는지 확인
        if (commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }
        
        // 좋아요 생성
        CommentLike commentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
        
        // 댓글의 좋아요 수 증가
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentService.save(comment);
        
        return commentLikeRepository.save(commentLike);
    }
    
    @Transactional
    public void removeLike(Long commentId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Comment comment = commentService.getCommentById(commentId);
        
        // 좋아요가 존재하는지 확인
        if (!commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new EntityNotFoundException("좋아요를 찾을 수 없습니다.");
        }
        
        // 좋아요 삭제
        commentLikeRepository.deleteByUserAndComment(user, comment);
        
        // 댓글의 좋아요 수 감소
        int likeCount = comment.getLikeCount();
        if (likeCount > 0) {
            comment.setLikeCount(likeCount - 1);
            commentService.save(comment);
        }
    }
    
    public boolean checkLikeStatus(Long commentId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Comment comment = commentService.getCommentById(commentId);
        
        return commentLikeRepository.existsByUserAndComment(user, comment);
    }
    
    @Transactional(readOnly = true)
    public Page<Comment> getLikedCommentsByUser(Long userId, Pageable pageable) {
        User user = userService.CheckUserAndGetUser(userId);
        return commentLikeRepository.findCommentsByUser(user, pageable);
    }
} 