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
    private String word;  // 단어

    @NotNull(message = "Meaning is required")
    @Size(min = 1, max = 500, message = "Meaning cannot be empty")
    private String meaning;  // 의미

    @NotNull(message = "Voca ID is required")
    private long vocaId;  // 보카 아이디
}


