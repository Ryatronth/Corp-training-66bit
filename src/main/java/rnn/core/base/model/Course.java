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
@Table(name = "course_t")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;

    private String description;

    private String pictureUrl;

    @ManyToOne
    private User author;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Convert(converter = TagConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "course_id")
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
