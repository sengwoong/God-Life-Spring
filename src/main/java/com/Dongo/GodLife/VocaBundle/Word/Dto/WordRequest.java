package com.Dongo.GodLife.VocaBundle.Word.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {

    @NotBlank(message = "Word is required")
    @Size(min = 1, max = 255, message = "Word cannot be empty")
    private String word;

    @NotBlank(message = "Meaning is required")
    @Size(min = 1, max = 500, message = "Meaning cannot be empty")
    private String meaning; 

    @NotNull(message = "Voca ID is required")
    @Positive(message = "Voca ID must be positive")
    private Long vocaId;
}


