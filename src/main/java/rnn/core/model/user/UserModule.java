package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Module;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private UserCourse course;

    @OneToMany(mappedBy = "module")
    private List<UserTopic> topics;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}
