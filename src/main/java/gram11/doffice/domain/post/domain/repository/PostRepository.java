package gram11.doffice.domain.post.domain.repository;

import gram11.doffice.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
