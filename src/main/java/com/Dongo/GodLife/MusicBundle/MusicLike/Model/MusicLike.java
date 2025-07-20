package com.Dongo.GodLife.MusicBundle.MusicLike.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.User.Model.User;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MusicLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicLikeId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "music_id")
    private Music music;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

}
