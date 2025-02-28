package com.Dongo.GodLife.PointBundle.Point.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.Dongo.GodLife.PointBundle.Point.Model.Point;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {
    private String title;
    private String content;
    private int points;
    private LocalDateTime createdAt;  
} 