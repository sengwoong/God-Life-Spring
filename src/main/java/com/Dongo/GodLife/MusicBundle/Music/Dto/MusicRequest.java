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
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", 
             message = "올바른 음악 URL 형식이 아닙니다")
    private String musicUrl;
}
