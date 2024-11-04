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
@Table(name = "tag_t")
@Entity
public class Tag {
    @Id
    private String name;

    private String color;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "course_tags",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name")}
    )
    private List<Course> courses;

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
