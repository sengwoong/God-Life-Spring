package com.Dongo.GodLife.PostBundle.CommentLike.Service;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.CommentLike.Model.CommentLike;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentLikePersistenceAdapter {
    CommentLike save(CommentLike commentLike);
    boolean existsByUserAndComment(User user, Comment comment);
    void deleteByUserAndComment(User user, Comment comment);
    Page<Comment> findCommentsByUser(User user, Pageable pageable);
} 