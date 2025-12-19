package gram11.doffice.domain.user.domain;

import gram11.doffice.domain.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    // BCrypt는 항상 60자 고정, length는 반드시 60 이상으로
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateUser(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
