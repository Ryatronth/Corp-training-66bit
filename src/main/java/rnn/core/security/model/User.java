package rnn.core.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import rnn.core.base.model.Course;

import java.util.List;
import java.util.Objects;

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
    @OneToMany
    @JoinColumn(name = "author_username")
    private List<Course> courses;

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
