package com.Dongo.GodLife.PostBundle.Post.Model;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.MusicBundle.Music.Model.Music;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String postContent;
    
    private String postImage;
    
    private String imageUrl;
    
    private int likeCount;
    
    private double price;
    
    private boolean sale;
    
    @Enumerated(EnumType.STRING)
    private PostType type;
    
    private boolean isShared;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private boolean isAdvertisement;
    
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonIgnore
    private List<Music> musicList = new ArrayList<>();
    
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonIgnore
    private List<Voca> vocaList = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        this.likeCount = 0;
        this.price = 0.0;
        this.sale = false;
        this.isShared = false;
    }
    
    public enum PostType {
        MUSIC, NORMAL, VOCA
    }
} 