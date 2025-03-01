package com.Dongo.GodLife.MusicBundle.Music.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicRequest {

    @NotBlank(message = "Music title cannot be blank")
    private String musicTitle;

    @NotBlank(message = "Music URL cannot be blank")
    private String musicUrl;

    
    private String imageUrl;

    @Builder.Default
    private String color = "#999999";

    @NotBlank(message = "Playlist ID cannot be blank")
    private Long playlistId;
}
