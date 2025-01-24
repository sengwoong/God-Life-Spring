package com.Dongo.GodLife.ScheduleBundle.Schedule.Dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class YearRangeValidator implements ConstraintValidator<YearRange, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull로 null 체크를 이미 하고 있으므로 여기서는 null을 유효한 값으로 처리
        }
        int year = value.getYear();
        return year >= 2000 && year <= 2100;
    }
}
