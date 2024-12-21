package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Course;
import rnn.core.model.security.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_course_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_username_course_id", columnNames = {"username", "course_id"})
        },
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_course_id", columnList = "course_id")
        }
)
@Entity
public class UserCourse {
    @Id
    @SequenceGenerator(name = "sequence_id_auto_gen", allocationSize = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id_auto_gen")
    private long id;

    private int currentScore;

    private boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserModule> userModules;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
