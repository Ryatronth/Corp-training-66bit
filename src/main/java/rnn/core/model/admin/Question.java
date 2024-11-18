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
        name = "question_t",
        indexes = {
                @Index(name = "idx_question_content_id", columnList = "content_id")
        }
)
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String question;

    @JsonIgnore
    @ManyToOne
    private Content content;
}
