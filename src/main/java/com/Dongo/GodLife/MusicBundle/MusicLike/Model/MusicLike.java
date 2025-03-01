package com.Dongo.GodLife.MusicBundle.MusicLike.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "music_likes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"music_id", "user_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
} 