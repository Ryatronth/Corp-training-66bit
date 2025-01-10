package rnn.core.model.admin;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "deadline_t",
        indexes = {
                @Index(name = "idx_deadline_module_group_id", columnList = "module_id, group_id"),
                @Index(name = "idx_deadline_groups_id", columnList = "group_id")
        }
)
@Entity
public class GroupDeadline {
    @Id
    @SequenceGenerator(name = "deadline_sequence_id_auto_gen", allocationSize = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deadline_sequence_id_auto_gen")
    private long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Override
    public String toString() {
        return "GroupDeadline{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
