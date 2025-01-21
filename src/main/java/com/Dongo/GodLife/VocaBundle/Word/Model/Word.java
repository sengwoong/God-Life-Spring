package com.Dongo.GodLife.VocaBundle.Word.Model;

import com.Dongo.GodLife.VocaBundle.Voca.Model.Voca;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    private String word;
    private String meaning;


    @ManyToOne()
    @JoinColumn(name = "voca_id", nullable = false)
    @JsonIgnore
    private Voca voca;

}
