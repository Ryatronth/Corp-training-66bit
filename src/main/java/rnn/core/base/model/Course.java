package rnn.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.base.model.converter.TagConverter;
import rnn.core.security.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "course_t",
        uniqueConstraints = {@UniqueConstraint(name = "unique_course", columnNames = "title")}
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Convert(converter = TagConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = {CascadeType.ALL}, orphanRemoval = true)
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", tags=" + tags +
                '}';
    }
}
