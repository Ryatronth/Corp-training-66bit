package rnn.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
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
