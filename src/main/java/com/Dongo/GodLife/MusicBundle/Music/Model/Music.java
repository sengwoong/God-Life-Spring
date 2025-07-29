package com.Dongo.GodLife.MusicBundle.Music.Model;

import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @NotBlank
    private String musicTitle;

    @NotBlank
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$|^https?://(www\\.)?youtube\\.com/watch\\?v=[\\w-]+(&.*)?$|^https?://youtu\\.be/[\\w-]+(\\?.*)?$", 
             message = "올바른 음악 URL 형식이 아닙니다")
    @Column(name = "music_url")
    private String musicUrl;

    private String imageUrl;

    @Builder.Default
    @NotBlank
    private String color = "#000000";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Playlist playlist;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
    