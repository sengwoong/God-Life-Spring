package com.Dongo.GodLife.PointBundle.Point.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointSummaryResponse {
    
    private Long userId;
    private Integer totalPoints;
    private Integer earnedPoints;
    private Integer usedPoints;
} 