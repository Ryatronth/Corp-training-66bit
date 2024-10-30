package rnn.core.base.model;

import jakarta.persistence.*;
import lombok.*;
import rnn.core.security.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course_t")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private String pictureUrl;

    @ManyToOne
    private User author;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
