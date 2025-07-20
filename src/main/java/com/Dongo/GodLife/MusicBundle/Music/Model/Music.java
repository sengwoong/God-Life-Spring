package com.Dongo.GodLife.MusicBundle.Music.Model;

import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicId;
    
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = true)
    private Playlist playlist;

    @NotBlank
    private String musicTitle;

    @NotBlank
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", 
             message = "올바른 음악 URL 형식이 아닙니다")
    @Column(name = "music_url")
    private String musicUrl;

}
