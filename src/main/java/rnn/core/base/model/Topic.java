package rnn.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "topic_t")
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private String title;

    private String description;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "topic_id")
    private List<Content> contents;
}
