package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.content.DetailedContent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "answer_t",
        indexes = {
                @Index(name = "idx_answer_content_id", columnList = "content_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_answer_content", columnNames = {"answer", "content_id"})
        }
)
@Entity
public class Answer {
    @Id
    @SequenceGenerator(name = "answer_sequence_id_auto_gen", allocationSize = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_sequence_id_auto_gen")
    private long id;

    @Column(length = 500)
    private String answer;

    private boolean isRight;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private DetailedContent content;
}
