package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Topic;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_topic_t",
        indexes = {
                @Index(name = "idx_user_module_id", columnList = "module_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_module_id_topic_id", columnNames = {"module_id", "topic_id"})
        }
)
@Entity
public class UserTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int currentScore;

    private int countAnsweredContents;

    private boolean isCompleted;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private UserModule module;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @JsonIgnore
    @OneToMany(mappedBy = "topic")
    private List<UserContent> contents;
}
