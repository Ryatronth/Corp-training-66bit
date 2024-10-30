package rnn.core.security.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role_t")
@Entity
public class Role implements GrantedAuthority {
    @Id
    @Enumerated(value = EnumType.STRING)
    private Name name;

    @OneToMany
    @JoinColumn(name = "role_name")
    private List<User> users;

    @Override
    public String getAuthority() {
        return name.name();
    }

    @Override
    public String toString() {
        return name.name();
    }

    @Getter
    public enum Name {
        ADMIN,
        USER
    }
}
