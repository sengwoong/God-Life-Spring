package com.Dongo.GodLife.PostBundle.CommentLike.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.CommentLike.Model.CommentLike;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserAndComment(User user, Comment comment);
    void deleteByUserAndComment(User user, Comment comment);
    
    @Query("SELECT cl.comment FROM CommentLike cl WHERE cl.user = :user")
    Page<Comment> findCommentsByUser(User user, Pageable pageable);
} 