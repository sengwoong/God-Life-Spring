package com.Dongo.GodLife.User.Model;

import com.example.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.example.GodLife.ProductBundle.Post.Model.Post;
import com.example.GodLife.ProductBundle.Product.Model.Product;
import com.example.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.example.GodLife.VocaBundle.Voca.Model.Voca;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickName;

    private int sales;

    private String phoneNumber;

    private String address;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Voca> voca = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Playlist> playlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product> Product;



    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // 현재 시간 자동 설정
    }
}
