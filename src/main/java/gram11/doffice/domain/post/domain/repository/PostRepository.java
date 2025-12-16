package gram11.doffice.domain.post.domain.repository;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.type.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostType(PostType postType);
}
