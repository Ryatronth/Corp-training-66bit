package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.converter.TagConverter;
import rnn.core.model.security.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "course_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_course", columnNames = "title")
        }
)
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;

    @Column(length = 360)
    private String description;

    private String pictureUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    private int score;

    @Convert(converter = TagConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Module> modules;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", author=" + author +
                ", score=" + score +
                ", tags=" + tags +
                '}';
    }
}
