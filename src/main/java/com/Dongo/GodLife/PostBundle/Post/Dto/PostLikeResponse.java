package com.Dongo.GodLife.PostBundle.Post.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {
    
    private Long postId;
    private Long userId;
    private boolean isLiked;
    private LocalDateTime createdAt;
    private int totalLikes;
} 