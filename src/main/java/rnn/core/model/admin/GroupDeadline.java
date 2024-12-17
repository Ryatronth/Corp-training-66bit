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
                @Index(name = "idx_deadline_module_group_id", columnList = "module_id, group_id")
        }
)
@Entity
public class GroupDeadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
