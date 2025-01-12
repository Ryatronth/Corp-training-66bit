package rnn.core.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Group;
import rnn.core.model.user.UserCourse;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(
        name = "user_t",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_git_hub_id", columnList = "git_hub_id"),
                @Index(name = "idx_git_lab_id", columnList = "git_lab_id")
        }
)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String username;

    @JsonIgnore
    private String gitHubId;

    @JsonIgnore
    private String gitLabId;

    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCourse> userCourses;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_group_t",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "group_id"),
            indexes = @Index(name = "idx_user_group_username", columnList = "username"),
            uniqueConstraints = @UniqueConstraint(name = "unique_user_group_t", columnNames = {"username", "group_id"})
    )
    private Set<Group> groups;

    @JsonProperty(value = "role")
    private String roleJson() {
        return role.getName().name();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", gitHubId='" + gitHubId + '\'' +
                ", gitLabId='" + gitLabId + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
