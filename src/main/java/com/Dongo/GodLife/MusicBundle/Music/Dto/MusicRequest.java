package com.Dongo.GodLife.MusicBundle.Music.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "음악 생성 요청 DTO")
public class MusicRequest {

    @NotBlank(message = "Music title cannot be blank")
    @Size(max = 255, message = "음악 제목은 255자를 초과할 수 없습니다")
    @Schema(description = "음악 제목", example = "Blizzards")
    private String musicTitle;

    @NotBlank(message = "Music URL cannot be blank")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$|^https?://(www\\.)?youtube\\.com/watch\\?v=[\\w-]+(&.*)?$|^https?://youtu\\.be/[\\w-]+(\\?.*)?$", 
             message = "올바른 음악 URL 형식이 아닙니다")
    @Schema(description = "음악 파일 URL", example = "https://www.youtube.com/watch?v=yKoBrB2dSbw")
    private String musicUrl;
    
    @Schema(description = "음악 이미지 URL", example = "https://example.com/music-image.jpg")
    private String imageUrl;
}
