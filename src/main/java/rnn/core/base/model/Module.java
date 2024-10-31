package rnn.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "module_t")
@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int position;

    private String title;

    private boolean isDone;

    @ManyToOne
    private Course course;

    @OneToMany
    @JoinColumn(name = "module_id")
    private List<Topic> topics;

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", position=" + position +
                ", title='" + title + '\'' +
                ", isDone=" + isDone +
                ", courseId=" + course.getId() +
                '}';
    }
}
