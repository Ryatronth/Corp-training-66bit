package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rnn.core.model.user.UserContent;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name = "content_t",
        indexes = {
                @Index(name = "idx_content_topic_id", columnList = "topic_id"),
                @Index(name = "idx_content_topic_id_position", columnList = "topic_id, position")
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

    protected String title;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    protected Topic topic;

    @JsonIgnore
    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<UserContent> userContents;

    public enum Type {
        SINGLE_ANSWER,
        MULTI_ANSWER,
        DETAILED_ANSWER,
        FREEFORM_ANSWER,
        VIDEO,
        PICTURE,
        TEXT
    }
}
