package com.Dongo.GodLife.PostBundle.Post.Service;

import com.Dongo.GodLife.PostBundle.Post.Dto.PostRequest;
import com.Dongo.GodLife.PostBundle.Post.Exception.NotYourPostException;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.Music.Service.MusicService;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.Dongo.GodLife.VocaBundle.Voca.Service.VocaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostPersistenceAdapter postRepository;
    private final MusicService musicService;
    private final VocaService vocaService;
    
    @Transactional
    public Post createPost(PostRequest postRequest, User user) {
        Post post = Post.builder()
                .user(user)
                .title(postRequest.getTitle())
                .postContent(postRequest.getPostContent())
                .postImage(postRequest.getPostImage())
                .imageUrl(postRequest.getImageUrl())
                .type(postRequest.getType())
                .isAdvertisement(postRequest.isAdvertisement())
                .build();
        
        post.setPrice(postRequest.getPrice());
        post.setSale(postRequest.isSale());
        post.setShared(postRequest.isShared());
        
        post = postRepository.save(post);
        
        if (postRequest.getMusicIds() != null && !postRequest.getMusicIds().isEmpty()) {
            for (Long musicId : postRequest.getMusicIds()) {
                Music music = musicService.getMusicById(musicId);
                music.setPost(post);
                musicService.saveMusic(music);
            }
        }
        
        if (postRequest.getVocaIds() != null && !postRequest.getVocaIds().isEmpty()) {
            for (Long vocaId : postRequest.getVocaIds()) {
                Voca voca = vocaService.findById(vocaId);
                voca.setPost(post);
                vocaService.saveVoca(voca);
            }
        }
        
        return post;
    }
    
    public Page<Post> getAllPostsByUserId(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }

    public Post getById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
    }
    
    @Transactional
    public Post updatePost(long postId, long userId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
        
        if (!post.getUser().getId().equals(userId)) {
            throw new NotYourPostException("Access denied: User does not own the post");
        }
        
        post.setTitle(postRequest.getTitle());
        post.setPostContent(postRequest.getPostContent());
        post.setPostImage(postRequest.getPostImage());
        post.setImageUrl(postRequest.getImageUrl());
        post.setPrice(postRequest.getPrice());
        post.setSale(postRequest.isSale());
        post.setType(postRequest.getType());
        post.setShared(postRequest.isShared());
        post.setAdvertisement(postRequest.isAdvertisement());
        
        if (postRequest.getMusicIds() != null) {
            for (Music music : post.getMusicList()) {
                music.setPost(null);
                musicService.save(music);
            }
            
            for (Long musicId : postRequest.getMusicIds()) {
                Music music = musicService.getMusicById(musicId);
                music.setPost(post);
                musicService.save(music);
            }
        }
        
        if (postRequest.getVocaIds() != null) {
            for (Voca voca : post.getVocaList()) {
                voca.setPost(null);
                vocaService.save(voca);
            }
            
            for (Long vocaId : postRequest.getVocaIds()) {
                Voca voca = vocaService.findById(vocaId);
                voca.setPost(post);
                vocaService.save(voca);
            }
        }
        
        return postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
        
        if (!post.getUser().getId().equals(userId)) {
            throw new NotYourPostException("Access denied: User does not own the post");
        }
        
        postRepository.delete(post);
    }
    
    @Transactional
    public Post toggleSharePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
        
        if (!post.getUser().getId().equals(userId)) {
            throw new NotYourPostException("Access denied: User does not own the post");
        }
        
        post.setShared(!post.isShared());
        
        return postRepository.save(post);
    }
    
    public Page<Post> getAllSharedPosts(Pageable pageable) {
        return postRepository.findByIsSharedTrue(pageable);
    }
    
    public Page<Post> getPostsByType(Post.PostType type, Pageable pageable) {
        return postRepository.findByType(type, pageable);
    }
    
    public Page<Post> getAdvertisementPosts(Pageable pageable) {
        return postRepository.findByIsAdvertisementTrue(pageable);
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }
} 