package com.Dongo.GodLife.VocaBundle.Word.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {

    @NotNull(message = "Word is required")
    @Size(min = 1, max = 255, message = "Word cannot be empty")
    private String word;

    @NotNull(message = "Meaning is required")
    @Size(min = 1, max = 500, message = "Meaning cannot be empty")
    private String meaning; 

    @NotNull(message = "Voca ID is required")
    private Long vocaId;
}


