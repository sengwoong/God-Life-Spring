package com.Dongo.GodLife.ScheduleBundle.Alarm.Model;

import com.Dongo.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alarm")
@Builder
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "alarm", fetch = FetchType.LAZY, optional = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Schedule schedule;

    @Column(nullable = false)
    private String alarmTitle;

    @Column(nullable = false)
    private String alarmcontent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
