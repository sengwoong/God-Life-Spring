package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.PostBundle.Post.Service.PostPersistenceAdapter;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostAdapterImpl implements PostPersistenceAdapter {
    
    private final PostRepository postRepository;
    
    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }
    
    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
    
    @Override
    public Optional<Post> findByUserIdAndPostId(Long userId, Long postId) {
        Post post = postRepository.findByUserIdAndPostId(userId, postId);
        return Optional.ofNullable(post);
    }
    
    @Override
    public Optional<Post> findByIsSharedTrueAndId(Long postId) {
        Post post = postRepository.findByIsSharedTrueAndId(postId);
        return Optional.ofNullable(post);
    }
    
    @Override
    public Page<Post> findByUser(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }
    
    @Override
    public Page<Post> findByUserAndType(User user, PostType type, Pageable pageable) {
        return postRepository.findByUserAndType(user, type, pageable);
    }
    
    @Override
    public Page<Post> findByIsSharedTrue(Pageable pageable) {
        return postRepository.findByIsSharedTrue(pageable);
    }
    
    @Override
    public Page<Post> findByIsAdTrue(Pageable pageable) {
        return postRepository.findByIsAdTrue(pageable);
    }
    
    @Override
    public Page<Post> findSharedPostsByUser(User user, Pageable pageable) {
        return postRepository.findSharedPostsByUser(user, pageable);
    }
    
    @Override
    public Page<Post> findAdPostsByUser(User user, Pageable pageable) {
        return postRepository.findAdPostsByUser(user, pageable);
    }
    
    @Override
    public Page<Post> findBySearch(String search, Pageable pageable) {
        return postRepository.findBySearch(search, pageable);
    }
    
    @Override
    public Page<Post> findByTypeAndSearch(PostType type, String search, Pageable pageable) {
        return postRepository.findByTypeAndSearch(type, search, pageable);
    }
    
    @Override
    public Page<Post> findByTypeAndSearchAndIsSharedTrue(PostType type, String search, Pageable pageable) {
        return postRepository.findByTypeAndSearchAndIsSharedTrue(type, search, pageable);
    }
    
    @Override
    public Page<Post> findByIsAdTrueAndIsSharedTrue(Pageable pageable) {
        return postRepository.findByIsAdTrueAndIsSharedTrue(pageable);
    }
    
    @Override
    public Page<Post> findAdPostsByUserAndIsSharedTrue(User user, Pageable pageable) {
        return postRepository.findAdPostsByUserAndIsSharedTrue(user, pageable);
    }
    
    @Override
    public Page<Post> findBySearchAndIsSharedTrue(String search, Pageable pageable) {
        return postRepository.findBySearchAndIsSharedTrue(search, pageable);
    }
    
    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
} 