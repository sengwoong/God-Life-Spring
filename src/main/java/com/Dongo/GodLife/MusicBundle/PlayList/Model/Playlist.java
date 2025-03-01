package com.Dongo.GodLife.MusicBundle.PlayList.Model;

import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String playlistTitle;
    private String imageUrl;
    
    private boolean isShared;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Music> musicList;

    @PrePersist
    protected void onCreate() {
        this.isShared = false; 
    }
}

