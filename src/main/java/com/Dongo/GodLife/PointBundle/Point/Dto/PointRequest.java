package com.Dongo.GodLife.PointBundle.Point.Dto;

import com.Dongo.GodLife.PointBundle.Point.Model.Point;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다")
    private String content;
    
    @NotNull(message = "포인트는 필수입니다")
    private Integer points;
    
    @NotNull(message = "포인트 타입은 필수입니다")
    private Point.PointType type;
} 