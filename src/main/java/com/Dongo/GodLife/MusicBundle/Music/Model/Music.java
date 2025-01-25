package com.Dongo.GodLife.MusicBundle.Music.Model;

import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = true)
    private Playlist playlist;

    private String musicTitle;
    private String musicUrl;

}
