package com.Dongo.GodLife.PostBundle.PostLike.Service;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
import com.Dongo.GodLife.PostBundle.PostLike.Model.PostLike;
import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.User.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    
    private final PostLikePersistenceAdapter postLikeRepository;
    private final PostService postService;
    private final UserService userService;
    
    @Transactional
    public PostLike addLike(Long postId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        // 이미 좋아요를 눌렀는지 확인
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }
        
        // 좋아요 생성
        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();
        
        // 게시물의 좋아요 수 증가
        post.setLikeCount(post.getLikeCount() + 1);
        postService.save(post);
        
        return postLikeRepository.save(postLike);
    }
    
    @Transactional
    public void removeLike(Long postId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        // 좋아요가 존재하는지 확인
        if (!postLikeRepository.existsByUserAndPost(user, post)) {
            throw new EntityNotFoundException("좋아요를 찾을 수 없습니다.");
        }
        
        // 좋아요 삭제
        postLikeRepository.deleteByUserAndPost(user, post);
        
        // 게시물의 좋아요 수 감소
        int likeCount = post.getLikeCount();
        if (likeCount > 0) {
            post.setLikeCount(likeCount - 1);
            postService.save(post);
        }
    }
    
    public boolean checkLikeStatus(Long postId, Long userId) {
        User user = userService.CheckUserAndGetUser(userId);
        Post post = postService.getById(postId);
        
        return postLikeRepository.existsByUserAndPost(user, post);
    }
} 