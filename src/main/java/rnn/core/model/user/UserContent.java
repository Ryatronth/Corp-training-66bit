package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Content;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_content_t"
)
@Entity
public class UserContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int currentAttempts;

    private boolean isDone;

    private boolean isSuccess;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private UserTopic topic;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
