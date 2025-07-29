package com.Dongo.GodLife.User.Model;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickName;

    private String phoneNumber;
    private String address;
    private String profileImage;
    private String bio;
    
    @Builder.Default
    private Integer level = 1;
    
    @Builder.Default
    private Integer followers = 0;
    
    @Builder.Default
    private Integer following = 0;
    
    // null 체크를 위한 메서드 추가
    public Integer getFollowers() {
        return followers != null ? followers : 0;
    }
    
    public Integer getFollowing() {
        return following != null ? following : 0;
    }
    
    private int sales;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Voca> voca;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Schedule> schedules;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String email) {
        this.email = email;
    }
}
