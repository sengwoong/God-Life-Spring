package com.Dongo.GodLife.PostBundle.Comment.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Comment.Service.CommentPersistenceAdapter;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentAdapterImpl implements CommentPersistenceAdapter {
    
    private final CommentRepository commentRepository;
    
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    
    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }
    
    @Override
    public Page<Comment> findByPost(Post post, Pageable pageable) {
        return commentRepository.findByPost(post, pageable);
    }
    
    @Override
    public Page<Comment> findByUser(User user, Pageable pageable) {
        return commentRepository.findByUser(user, pageable);
    }
    
    @Override
    public Page<Comment> findByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }
    
    @Override
    public Long countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
} 