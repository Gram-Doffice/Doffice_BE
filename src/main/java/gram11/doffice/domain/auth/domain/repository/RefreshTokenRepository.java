package gram11.doffice.domain.auth.domain.repository;

import gram11.doffice.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
}
