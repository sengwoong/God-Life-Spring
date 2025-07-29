package com.Dongo.GodLife.PostBundle.Comment.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    
    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 500, message = "댓글 내용은 500자를 초과할 수 없습니다")
    private String content;
    
    @NotNull(message = "게시물 ID는 필수입니다")
    @Positive(message = "게시물 ID는 양수여야 합니다")
    private Long postId;
} 