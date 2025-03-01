package com.Dongo.GodLife.PostBundle.Comment.Service;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentPersistenceAdapter {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    void deleteById(Long id);
    Page<Comment> findByPost(Post post, Pageable pageable);
    List<Comment> findByUser(User user);
} 