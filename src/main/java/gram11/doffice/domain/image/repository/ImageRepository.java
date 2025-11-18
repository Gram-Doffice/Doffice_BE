package gram11.doffice.domain.image.repository;

import gram11.doffice.domain.image.entity.LostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<LostImage, Long> {

}
