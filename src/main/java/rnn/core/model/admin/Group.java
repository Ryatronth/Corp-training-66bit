package rnn.core.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.security.User;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    private boolean isDefault;

    private int countMembers;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<GroupDeadline> deadlines;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_group_t",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(name = "idx_user_id_group_username", columnList = "user_id"),
            uniqueConstraints = @UniqueConstraint(name = "unique_user_id_group_t", columnNames = {"user_id", "group_id"})
    )
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id && isDefault == group.isDefault && countMembers == group.countMembers && Objects.equals(name, group.name) && Objects.equals(course, group.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isDefault, countMembers, course);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDefault=" + isDefault +
                ", countMembers=" + countMembers +
                '}';
    }
}
