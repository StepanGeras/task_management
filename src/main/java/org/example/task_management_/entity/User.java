package org.example.task_management_.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

@Entity
@Data
@Table(name = "newUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @ElementCollection
    private Set<Role> roles;

    public enum Role implements GrantedAuthority {
        ROLE_ADMIN, ROLE_USER;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }

}

