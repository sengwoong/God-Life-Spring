package com.Dongo.GodLife.ScheduleBundle.Schedule.Dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YearRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YearRange {
    String message() default "Date must be between 2000 and 2100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
