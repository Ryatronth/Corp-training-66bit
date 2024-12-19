package rnn.core.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Course;
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
@Table(name = "user_t")
@Entity
public class User {
    @Id
    private String username;

    private String email;

    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Course> courses;

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
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(avatarUrl, user.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, avatarUrl);
    }
}
