package gram11.doffice.domain.lost.repository;

import gram11.doffice.domain.lost.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostRepository extends JpaRepository<LostPost, Long> {

}