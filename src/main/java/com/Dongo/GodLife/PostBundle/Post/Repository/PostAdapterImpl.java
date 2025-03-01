package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Exception.NotYourPostException;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostAdapterImpl implements PostPersistenceAdapter {
    
    private final PostRepository postRepository;
    
    public PostAdapterImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }
    
    @Override
    public Page<Post> findByUser(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }
    
    @Override
    public Optional<Post> findById(long postId) {
        return postRepository.findById(postId);
    }
    
    @Override
    public Post delete(Post post) throws NotYourPostException {
        if (post == null) {
            throw new NotYourPostException("Post cannot be null");
        }
        postRepository.delete(post);
        return post;
    }
    
    @Override
    public Page<Post> findByIsSharedTrue(Pageable pageable) {
        return postRepository.findByIsSharedTrue(pageable);
    }
    
    @Override
    public Page<Post> findByType(Post.PostType type, Pageable pageable) {
        return postRepository.findByType(type, pageable);
    }
    
    @Override
    public Page<Post> findByIsAdvertisementTrue(Pageable pageable) {
        return postRepository.findByIsAdvertisementTrue(pageable);
    }
} 