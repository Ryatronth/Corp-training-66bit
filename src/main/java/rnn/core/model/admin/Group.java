package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.user.UserCourse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "group_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_group_name_course", columnNames = {"name", "course_id"})
        },
        indexes = {
                @Index(name = "idx_group_course_id", columnList = "course_id")
        }
)
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST})
    private List<UserCourse> userCourses;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<GroupDeadline> deadlines;
}
