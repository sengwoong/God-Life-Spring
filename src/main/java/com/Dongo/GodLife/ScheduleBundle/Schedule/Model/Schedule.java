package com.Dongo.GodLife.ScheduleBundle.Schedule.Model;

import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private String time;

    @Column(name = "day")
    private String day;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "has_alarm")
    private boolean hasAlarm;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

 
}
