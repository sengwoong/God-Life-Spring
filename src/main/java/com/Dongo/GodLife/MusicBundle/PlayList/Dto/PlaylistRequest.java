package com.Dongo.GodLife.MusicBundle.PlayList.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequest {

    @NotBlank(message = "Playlist title cannot be blank")
    @Size(max = 255, message = "플레이리스트 제목은 255자를 초과할 수 없습니다")
    private String playListTitle;
    
    @Pattern(regexp = "^(https?://.*|)$", message = "이미지 URL은 유효한 HTTP/HTTPS URL이어야 합니다")
    private String imageUrl;
    
    private Boolean shared = false;
}
