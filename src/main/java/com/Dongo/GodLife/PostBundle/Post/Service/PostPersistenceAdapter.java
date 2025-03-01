package com.Dongo.GodLife.PostBundle.Post.Service;

import com.Dongo.GodLife.PostBundle.Post.Exception.NotYourPostException;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostPersistenceAdapter {
    Post save(Post post);
    
    Page<Post> findByUser(User user, Pageable pageable);
    
    Optional<Post> findById(long postId);
    
    Post delete(Post post) throws NotYourPostException;
    
    Page<Post> findByIsSharedTrue(Pageable pageable);
    
    Page<Post> findByType(Post.PostType type, Pageable pageable);
    
    Page<Post> findByIsAdvertisementTrue(Pageable pageable);
} 