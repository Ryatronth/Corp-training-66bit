package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "answer_t",
        indexes = {
                @Index(name = "idx_answer_content_id", columnList = "content_id")
        }
)
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String answer;

    @JsonIgnore
    @ManyToOne
    private Content content;
}
