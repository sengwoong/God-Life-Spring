package com.Dongo.GodLife.MusicBundle.PlayList.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequest {

    @NotBlank(message = "Playlist title cannot be blank")
    private String playListTitle;
}
