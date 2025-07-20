package com.Dongo.GodLife.MusicBundle.MusicLike.Dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "좋아요한 음악 응답 DTO")
public class LikedMusicResponse {

    @Schema(description = "음악 ID", example = "1")
    private Long musicId;

    @Schema(description = "음악 제목", example = "Blizzards")
    private String musicTitle;

    @Schema(description = "음악 URL", example = "https://example.com/music/blizzards.mp3")
    private String musicUrl;

    @Schema(description = "앨범 아트 이미지 URL", example = "https://example.com/images/blizzards.jpg")
    private String imageUrl;

    @Schema(description = "음악 테마 색상", example = "#FF6B6B")
    private String color;

    @Schema(description = "좋아요 생성일시", example = "2024-01-01T00:00:00")
    private LocalDateTime likedAt;
}