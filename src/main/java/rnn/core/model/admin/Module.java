package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import rnn.core.model.user.UserModule;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "module_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_title_course", columnNames = {"title", "course_id"}),
        },
        indexes = {
                @Index(name = "idx_module_course_id", columnList = "course_id"),
                @Index(name = "idx_module_course_id_position", columnList = "course_id, position")
        }
)
@DynamicUpdate
@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private String title;

    private int score;

    private int countAnsweredContents;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "module", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Topic> topics;

    @JsonIgnore
    @OneToMany(mappedBy = "module", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserModule> userModules;

    @JsonIgnore
    @OneToMany(mappedBy = "module", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<GroupDeadline> deadlines;
}
