package com.Dongo.GodLife.PostBundle.Post.Dto;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String postContent;
    
    private String postImage;
    
    private String imageUrl;
    
    private double price;
    
    private boolean sale;
    
    private Post.PostType type;
    
    private boolean isShared;
    
    private boolean isAdvertisement;
    
    private List<Long> musicIds;
    
    private List<Long> vocaIds;
} 