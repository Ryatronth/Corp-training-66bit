package rnn.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name = "content_t",
        indexes = {
                @Index(name = "idx_content_topic_id", columnList = "topic_id")
        }
)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected int position;

    @Enumerated(EnumType.STRING)
    protected Type type;

    protected int score;

    protected String title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    protected Topic topic;

    public enum Type {
        SINGLE_ANSWER,
        MULTI_ANSWER,
        DETAILED_ANSWER,
        VIDEO,
        PICTURE,
        TEXT
    }
}
