package com.Dongo.GodLife.PostBundle.Post.Dto;

import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 2000, message = "내용은 2000자를 초과할 수 없습니다")
    private String content;
    
    @Pattern(regexp = "^(https?://.*|)$", message = "이미지 URL은 유효한 HTTP/HTTPS URL이어야 합니다")
    private String imageUrl;
    
    @NotNull(message = "게시물 타입은 필수입니다")
    private PostType type;
    
    @NotNull(message = "게시물 카테고리는 필수입니다")
    private PostCategory category;
    
    private Boolean isShared = false;
    
    private Boolean isAd = false;
} 