package com.Dongo.GodLife.ScheduleBundle.Schedule.Dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class YearRangeValidator implements ConstraintValidator<YearRange, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }
        int year = value.getYear();
        return year >= 2000 && year <= 2100;
    }
}
