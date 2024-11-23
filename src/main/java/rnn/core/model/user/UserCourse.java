package rnn.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Group;
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
                @Index(name = "idx_username", columnList = "username")
        }
)
@Entity
public class UserCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int currentScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserModule> userModules;
}
