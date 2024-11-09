package rnn.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "content_t")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private Type type;

    private String title;

    private String description;

    private int countAttempts;

    private String videoUrl;

    private int score;

    @ManyToOne
    private Topic topic;

    @OneToMany
    @JoinColumn(name = "content_id")
    private List<Question> questions;

    @OneToMany
    @JoinColumn(name = "content_id")
    private List<Answer> answers;

    public enum Type {
        SINGLE_ANSWER,
        MULTI_ANSWER,
        DETAILED_ANSWER,
        VIDEO,
        TEXT
    }
}
