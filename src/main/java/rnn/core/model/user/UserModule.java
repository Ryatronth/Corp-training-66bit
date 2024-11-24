package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.GroupDeadline;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_module_t",
        indexes = {
                @Index(name = "idx_user_course_id", columnList = "course_id")
        }
)
@Entity
public class UserModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int currentScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deadline_id")
    private GroupDeadline deadline;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private UserCourse course;

    @JsonIgnore
    @OneToMany(mappedBy = "module")
    private List<UserTopic> topics;
}
