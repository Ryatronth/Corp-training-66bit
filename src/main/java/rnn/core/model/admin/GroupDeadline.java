package rnn.core.model.admin;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.user.UserModule;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @JsonIgnore
    @OneToMany(mappedBy = "deadline")
    private List<UserModule> userModules;
}
