package com.Dongo.GodLife.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse {
    
    private Long followerId;
    private Long followingId;
    private boolean isFollowing;
    private LocalDateTime createdAt;
    private int followerCount;
    private int followingCount;
} 