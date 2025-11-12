package gram11.doffice.domain.image.repository;

import gram11.doffice.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // id 기반 조회로 변경
    List<Image> findAllByNoticeId(Long noticeId);
}
