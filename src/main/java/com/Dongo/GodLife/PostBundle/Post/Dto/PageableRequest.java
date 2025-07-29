package com.Dongo.GodLife.PostBundle.Post.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {
    private Integer page = 0;
    private Integer size = 10;
    private List<String> sort;
} 