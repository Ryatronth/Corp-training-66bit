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
    private long id;

    private int position;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
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
