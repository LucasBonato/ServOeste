package com.serv.oeste.infrastructure.entities.user;

import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "Username"))
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Username", unique = true, length = 100)
    private String username;

    @Column(name = "Password_Hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Roles role;

    public UserEntity(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.role = user.getRole();
    }

    public User toUser() {
        return User.restore(
            id,
            username,
            passwordHash,
            role
        );
    }
}
