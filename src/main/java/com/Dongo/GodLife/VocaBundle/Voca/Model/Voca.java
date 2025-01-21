package com.Dongo.GodLife.VocaBundle.Voca.Model;
import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vocaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    private String vocaTitle;
    private String description;
    private LocalDateTime createdAt;


}
