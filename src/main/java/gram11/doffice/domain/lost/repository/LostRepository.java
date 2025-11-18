package gram11.doffice.domain.lost.repository;

import gram11.doffice.domain.lost.entity.Lost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostRepository extends JpaRepository<Lost, Long> {
}
