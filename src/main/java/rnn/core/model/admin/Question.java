package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.content.AnswerContent;

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
    @SequenceGenerator(name = "sequence_id_auto_gen", allocationSize = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id_auto_gen")
    private long id;

    @Column(length = 500)
    private String question;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private AnswerContent content;
}
