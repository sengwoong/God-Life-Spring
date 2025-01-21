package com.Dongo.GodLife.VocaBundle.Voca.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocaRequest {
    @NotNull(message = "Title is required")
    @Size(min = 1,max=255, message = "Title cannot be empty")
    @JsonProperty("vocaTitle")
    private String vocaTitle;

    @NotNull(message = "Description is required")
    @Size(min = 1,max=255, message = "Description cannot be empty")
    @JsonProperty("description")
    private String description;

}