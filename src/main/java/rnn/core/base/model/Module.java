package rnn.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "module_t",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "course_id"})
)
@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private String title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "module_id")
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
