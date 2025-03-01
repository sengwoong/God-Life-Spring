package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable pageable);
    
    Page<Post> findByIsSharedTrue(Pageable pageable);
    
    Page<Post> findByType(Post.PostType type, Pageable pageable);
    
    Page<Post> findByIsAdvertisementTrue(Pageable pageable);
} 