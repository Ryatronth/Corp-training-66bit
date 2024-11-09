package rnn.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "topic_t",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_title_module", columnNames = {"title", "module_id"}),
                @UniqueConstraint(name = "unique_position_module", columnNames = {"position", "module_id"})
        }
)
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private String title;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;

//    @JsonIgnore
//    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
//    @JoinColumn(name = "topic_id")
//    private List<Content> contents;
}
