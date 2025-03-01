package com.Dongo.GodLife.PostBundle.PostLike.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.PostLike.Model.PostLike;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
} 