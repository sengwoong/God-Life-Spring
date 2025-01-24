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

    @NotNull(message = "Start time cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "Start time must be in the present or future")
    @YearRange(message = "Start time must be between 2000 and 2100")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future(message = "End time must be in the future")
    @YearRange(message = "End time must be between 2000 and 2100")
    private LocalDateTime endTime;

    @AssertTrue(message = "Start time must be before or equal to end time")
    public boolean isStartTimeBeforeOrEqualEndTime() {
        if (startTime == null || endTime == null) {
            return true; // 다른 검증에서 null 여부를 처리함
        }
        return !startTime.isAfter(endTime);
    }
}
