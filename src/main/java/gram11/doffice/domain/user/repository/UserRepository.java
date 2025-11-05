package gram11.doffice.domain.user.repository;

import gram11.doffice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 찾는 거
    Optional<User> findByUsername(String username);
}
