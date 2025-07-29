package com.Dongo.GodLife.ScheduleBundle.Schedule.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {

    @NotBlank(message = "Schedule title cannot be blank")
    @Size(max = 255, message = "Schedule title must be 255 characters or less")
    private String scheduleTitle;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 1000, message = "Content must be 1000 characters or less")
    private String content;

    @NotBlank(message = "Start time cannot be blank")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Start time must be in HH:MM format")
    private String startTime;

    @NotBlank(message = "End time cannot be blank")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "End time must be in HH:MM format")
    private String endTime;

    @NotBlank(message = "Day cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Day must be in YYYY-MM-DD format")
    private String day;

    private boolean hasAlarm;
}
