package com.Dongo.GodLife.PostBundle.PostLike.Model;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
} 