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

    private boolean isCompleted;

    private boolean isSuccess;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private UserTopic topic;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @JsonIgnore
    @OneToOne(mappedBy = "content", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private UserAnswer answer;
}
