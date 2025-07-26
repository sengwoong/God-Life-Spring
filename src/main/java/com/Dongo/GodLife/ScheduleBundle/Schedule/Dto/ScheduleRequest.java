package com.Dongo.GodLife.ScheduleBundle.Schedule.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {

    @NotBlank(message = "Schedule title cannot be blank")
    @Size(max = 255, message = "Schedule title must be 255 characters or less")
    private String scheduleTitle;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotBlank(message = "Start time cannot be blank")
    private String startTime;

    @NotBlank(message = "End time cannot be blank")
    private String endTime;

    @NotBlank(message = "Day cannot be blank")
    private String day;

    private boolean hasAlarm = false;
}
