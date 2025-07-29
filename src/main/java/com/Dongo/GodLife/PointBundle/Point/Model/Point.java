package com.Dongo.GodLife.PointBundle.Point.Model;

import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    private Integer points;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum PointType {
        EARN, USE
    }
} 