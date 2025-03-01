package com.Dongo.GodLife.PostBundle.CommentLike.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.CommentLike.Model.CommentLike;
import com.Dongo.GodLife.PostBundle.CommentLike.Service.CommentLikePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentLikeAdapterImpl implements CommentLikePersistenceAdapter {
    
    private final CommentLikeRepository commentLikeRepository;
    
    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeRepository.save(commentLike);
    }
    
    @Override
    public boolean existsByUserAndComment(User user, Comment comment) {
        return commentLikeRepository.existsByUserAndComment(user, comment);
    }
    
    @Override
    public void deleteByUserAndComment(User user, Comment comment) {
        commentLikeRepository.deleteByUserAndComment(user, comment);
    }
    
    @Override
    public Page<Comment> findCommentsByUser(User user, Pageable pageable) {
        return commentLikeRepository.findCommentsByUser(user, pageable);
    }
} 