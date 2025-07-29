package com.Dongo.GodLife.PostBundle.Post.Service;

import com.Dongo.GodLife.PostBundle.Post.Dto.PostLikeResponse;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.PostLike;
import com.Dongo.GodLife.PostBundle.Post.Repository.PostLikeRepository;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    
    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    
    /**
     * 공유된 포스트 좋아요 토글
     */
    public PostLikeResponse toggleLike(Long postId, User user) {
        Post post = postService.findSharedById(postId);
        
        // 이미 좋아요를 눌렀는지 확인
        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId, user.getId());
        
        if (isLiked) {
            // 좋아요 취소
            postLikeRepository.deleteByPostIdAndUserId(postId, user.getId());
            post.setLikeCount(post.getLikeCount() - 1);
            postService.savePost(post);
            
            PostLikeResponse response = new PostLikeResponse();
            response.setPostId(postId);
            response.setUserId(user.getId());
            response.setLiked(false);
            response.setTotalLikes(post.getLikeCount());
            return response;
        } else {
            // 좋아요 추가
            PostLike postLike = new PostLike();
            postLike.setPost(post);
            postLike.setUser(user);
            postLike.setCreatedAt(LocalDateTime.now());
            
            postLikeRepository.save(postLike);
            post.setLikeCount(post.getLikeCount() + 1);
            postService.savePost(post);
            
            PostLikeResponse response = new PostLikeResponse();
            response.setPostId(postId);
            response.setUserId(user.getId());
            response.setLiked(true);
            response.setCreatedAt(postLike.getCreatedAt());
            response.setTotalLikes(post.getLikeCount());
            return response;
        }
    }
    

    public boolean isLikedByUser(Long postId, User user) {
        return postLikeRepository.existsByPostIdAndUserId(postId, user.getId());
    }
    

    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public List<PostLike> getLikedPostsByUser(Long userId) {
        return postLikeRepository.findByUserId(userId);
    }
    

    public List<PostLike> getLikesByPost(Long postId) {
        return postLikeRepository.findByPostId(postId);
    }
    

    public void deleteLikesByPost(Long postId) {
        postLikeRepository.deleteByPostId(postId);
    }
} 