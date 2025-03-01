package com.Dongo.GodLife.MusicBundle.Music.Model;

import com.Dongo.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
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

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    private String musicTitle;

    private String musicUrl;

    private String imageUrl;
    
    private String color;

}
