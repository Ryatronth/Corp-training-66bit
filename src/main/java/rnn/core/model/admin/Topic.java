package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "topic_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_title_module", columnNames = {"title", "module_id"}),
        },
        indexes = {
                @Index(name = "idx_topic_module_id", columnList = "module_id"),
                @Index(name = "idx_topic_module_id_position", columnList = "module_id, position")
        }
)
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private String title;

    private int score;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;

    @JsonIgnore
    @OneToMany(mappedBy = "topic", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Content> contents;
}
