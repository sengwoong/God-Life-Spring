package com.Dongo.GodLife.MusicBundle.Music.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicRequest {

    @NotBlank(message = "Music title cannot be blank")
    private String musicTitle;

    @NotBlank(message = "Music URL cannot be blank")
    private String musicUrl;
}
