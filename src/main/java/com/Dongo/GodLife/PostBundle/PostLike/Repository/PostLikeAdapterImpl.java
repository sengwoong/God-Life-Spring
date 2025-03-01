package com.Dongo.GodLife.PostBundle.PostLike.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.PostLike.Model.PostLike;
import com.Dongo.GodLife.PostBundle.PostLike.Service.PostLikePersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PostLikeAdapterImpl implements PostLikePersistenceAdapter {
    
    private final PostLikeRepository postLikeRepository;
    
    public PostLikeAdapterImpl(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }
    
    @Override
    public PostLike save(PostLike postLike) {
        return postLikeRepository.save(postLike);
    }
    
    @Override
    public boolean existsByUserAndPost(User user, Post post) {
        return postLikeRepository.existsByUserAndPost(user, post);
    }
    
    @Override
    @Transactional
    public void deleteByUserAndPost(User user, Post post) {
        postLikeRepository.deleteByUserAndPost(user, post);
    }
} 