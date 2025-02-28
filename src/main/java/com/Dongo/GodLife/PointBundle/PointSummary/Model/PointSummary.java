package com.Dongo.GodLife.PointBundle.PointSummary.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointSummary {
    
    @Id
    private Long userId;
    
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalPoints;
    
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer earnedPoints;
    
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer usedPoints;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
} 