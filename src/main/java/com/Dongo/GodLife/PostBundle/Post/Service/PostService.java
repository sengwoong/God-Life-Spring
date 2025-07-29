package com.Dongo.GodLife.PostBundle.Post.Service;

import com.Dongo.GodLife.PostBundle.Post.Dto.PostRequest;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostPersistenceAdapter postPersistenceAdapter;
    
    public Post createPost(PostRequest request, User user) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setType(request.getType());
        post.setCategory(request.getCategory());
        post.setIsShared(request.getIsShared());
        post.setIsAd(request.getIsAd());
        post.setUser(user);
        
        return postPersistenceAdapter.save(post);
    }
    
    // 공유된 포스트 찾기 (아무나 볼 수 있는 공유된 게시물)
    public Post findSharedById(Long postId) {
        return postPersistenceAdapter.findByIsSharedTrueAndId(postId)
                .orElseThrow(() -> new EntityNotFoundException("공유된 게시물을 찾을 수 없습니다"));
    }
    
    // 나의 포스트 찾기 (나만 볼 수 있는 내 게시물)
    public Post findMyPostById(Long postId, User user) {
        return postPersistenceAdapter.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new EntityNotFoundException("내 게시물을 찾을 수 없습니다"));
    }
    
    // 나의 포스트 목록 조회 (내가 쓴 모든 게시물)
    public Page<Post> getMyPostsByType(User user, PostType type, Pageable pageable) {
        return postPersistenceAdapter.findByUserAndType(user, type, pageable);
    }
    
    
    public Page<Post> getPostsByTypeAndSearch(PostType type, String search, Pageable pageable) {
        // 공유된 게시물만 검색
        return postPersistenceAdapter.findByTypeAndSearchAndIsSharedTrue(type, search, pageable);
    }
    
    public Page<Post> getSharedPosts(Pageable pageable) {
        // shared 을 한 게시물
        return postPersistenceAdapter.findByIsSharedTrue(pageable);
    }
    
    public Page<Post> getAdPosts(Pageable pageable) {
        // 공유된 광고 게시물만
        return postPersistenceAdapter.findByIsAdTrueAndIsSharedTrue(pageable);
    }
    
    public Page<Post> getSharedPostsByUser(User user, Pageable pageable) {
        // shared 한것중 내가 쓴 게시물
        return postPersistenceAdapter.findSharedPostsByUser(user, pageable);
    }
    
    public Page<Post> getAdPostsByUser(User user, Pageable pageable) {
        // 공유된 광고 게시물만
        return postPersistenceAdapter.findAdPostsByUserAndIsSharedTrue(user, pageable);
    }
    
    public Page<Post> getBestPosts(Pageable pageable) {
        // 공유된 베스트 게시물만
        return postPersistenceAdapter.findBySearchAndIsSharedTrue("", pageable);
    }
    
    public Post updatePost(Long postId, PostRequest request, User user) {
        Post post = findMyPostById(postId, user);
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setType(request.getType());
        post.setCategory(request.getCategory());
        post.setIsShared(request.getIsShared());
        post.setIsAd(request.getIsAd());
        
        return postPersistenceAdapter.save(post);
    }
    
    public Post toggleAdStatus(Long postId, User user) {
        Post post = findMyPostById(postId, user);
        
        post.setIsAd(!post.getIsAd());
        return postPersistenceAdapter.save(post);
    }
    
    public void deletePost(Long postId, User user) {
        Post post = findMyPostById(postId, user);
        
        postPersistenceAdapter.delete(post);
    }
    
    public void updateCommentCount(Long postId, int count) {
        Post post = findSharedById(postId);
        post.setCommentCount(count);
        postPersistenceAdapter.save(post);
    }
    
    public Post savePost(Post post) {
        return postPersistenceAdapter.save(post);
    }
} 