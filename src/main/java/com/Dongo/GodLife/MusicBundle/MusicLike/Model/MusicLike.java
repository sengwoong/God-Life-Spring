package com.Dongo.GodLife.MusicBundle.MusicLike.Model;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicLikeId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Music music;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
