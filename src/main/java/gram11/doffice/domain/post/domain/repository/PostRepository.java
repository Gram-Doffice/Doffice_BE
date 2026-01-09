package gram11.doffice.domain.post.domain.repository;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.type.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPostType(PostType postType, Pageable pageable);
}
