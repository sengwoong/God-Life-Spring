package com.Dongo.GodLife.PostBundle.Comment.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByPost(Post post, Pageable pageable);
    
    Page<Comment> findByUser(User user, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
    Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);
} 