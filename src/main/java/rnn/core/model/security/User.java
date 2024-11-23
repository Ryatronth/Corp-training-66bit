package rnn.core.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.model.admin.Course;
import rnn.core.model.user.UserCourse;

import java.util.List;

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
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Course> courses;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCourse> userCourses;

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
}
