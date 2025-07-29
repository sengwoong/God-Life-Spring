package com.Dongo.GodLife.PostBundle.Post.Service;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostPersistenceAdapter {
    
    Post save(Post post);
    
    Optional<Post> findById(Long postId);
    
    Optional<Post> findByUserIdAndPostId(Long userId, Long postId);
    
    Optional<Post> findByIsSharedTrueAndId(Long postId);
    
    Page<Post> findByUser(User user, Pageable pageable);
    
    Page<Post> findByUserAndType(User user, PostType type, Pageable pageable);
    
    Page<Post> findByIsSharedTrue(Pageable pageable);
    
    Page<Post> findByIsAdTrue(Pageable pageable);
    
    Page<Post> findSharedPostsByUser(User user, Pageable pageable);
    
    Page<Post> findAdPostsByUser(User user, Pageable pageable);
    
    Page<Post> findBySearch(String search, Pageable pageable);
    
    Page<Post> findByTypeAndSearch(PostType type, String search, Pageable pageable);
    
    Page<Post> findByTypeAndSearchAndIsSharedTrue(PostType type, String search, Pageable pageable);
    
    Page<Post> findByIsAdTrueAndIsSharedTrue(Pageable pageable);
    
    Page<Post> findAdPostsByUserAndIsSharedTrue(User user, Pageable pageable);
    
    Page<Post> findBySearchAndIsSharedTrue(String search, Pageable pageable);
    
    void delete(Post post);
} 