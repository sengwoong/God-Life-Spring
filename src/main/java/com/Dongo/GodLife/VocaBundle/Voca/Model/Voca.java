package com.Dongo.GodLife.VocaBundle.Voca.Model;

import com.Dongo.GodLife.User.Model.User;
import com.Dongo.GodLife.VocaBundle.Word.Model.Word;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vocaId;

    @NotBlank
    private String vocaTitle;

    @NotBlank
    private String languages;

    private String description;
    private LocalDateTime createdAt;
    
    @Builder.Default
    private Boolean isShared = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "voca", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Word> words;
}
