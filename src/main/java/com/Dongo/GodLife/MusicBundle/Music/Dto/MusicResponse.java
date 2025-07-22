package com.Dongo.GodLife.MusicBundle.Music.Dto;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "음악 응답 DTO")
public class MusicResponse {

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

    @Schema(description = "플레이리스트 정보")
    private PlaylistInfo playlist;

    @Schema(description = "생성일시", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-01-01T00:00:00")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @NotNull
    @Schema(description = "플레이리스트 정보")
    public static class PlaylistInfo {
        @Schema(description = "플레이리스트 ID", example = "1")
        private Long playlistId;

        @Schema(description = "플레이리스트 제목", example = "내 플레이리스트")
        private String title;
    }

    public static MusicResponse from(Music music) {
        PlaylistInfo playlistInfo = null;
        if (music.getPlaylist() != null) {
            playlistInfo = PlaylistInfo.builder()
                    .playlistId(music.getPlaylist().getPlaylistId())
                    .title(music.getPlaylist().getPlaylistTitle())
                    .build();
        }

        return MusicResponse.builder()
                .musicId(music.getMusicId())
                .musicTitle(music.getMusicTitle())
                .musicUrl(music.getMusicUrl())
                .imageUrl(music.getImageUrl())
                .color(music.getColor())
                .playlist(playlistInfo)
                .build();
    }
} 