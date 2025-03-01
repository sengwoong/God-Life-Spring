package com.Dongo.GodLife.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
    
    public static ErrorResponse of(String message, int status, String error) {
        return ErrorResponse.builder()
                .message(message)
                .status(status)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
} 