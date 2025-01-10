package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.user.UserTopic;

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

    private int countAnsweredContents;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @JsonIgnore
    @OneToMany(mappedBy = "topic", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Content> contents;

    @JsonIgnore
    @OneToMany(mappedBy = "topic", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserTopic> userTopics;

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", position=" + position +
                ", title='" + title + '\'' +
                ", score=" + score +
                ", countAnsweredContents=" + countAnsweredContents +
                '}';
    }
}
