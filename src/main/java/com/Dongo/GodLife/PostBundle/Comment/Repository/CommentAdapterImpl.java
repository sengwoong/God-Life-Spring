package com.Dongo.GodLife.PostBundle.Comment.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Comment.Service.CommentPersistenceAdapter;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentAdapterImpl implements CommentPersistenceAdapter {
    
    private final CommentRepository commentRepository;
    
    public CommentAdapterImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    
    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }
    
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
    
    @Override
    public Page<Comment> findByPost(Post post, Pageable pageable) {
        return commentRepository.findByPost(post, pageable);
    }
    
    @Override
    public List<Comment> findByUser(User user) {
        return commentRepository.findByUser(user);
    }
} 