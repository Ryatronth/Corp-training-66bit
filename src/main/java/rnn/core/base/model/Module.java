package rnn.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
                @UniqueConstraint(name = "unique_position_course", columnNames = {"position", "course_id"})
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "module", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Topic> topics;

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", position=" + position +
                ", title='" + title + '\'' +
                ", courseId=" + course.getId() +
                '}';
    }
}
