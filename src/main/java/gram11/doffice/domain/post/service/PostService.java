package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.exception.NoAuthorException;
import gram11.doffice.domain.post.presentaton.dto.response.PostDetailResponse;
import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        return PostDetailResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createAt(post.getCreatedAt())
                .type(post.getPostType().toString())
                .imageUrl(post.getImageUrl())
                .build();
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostListResponse> getAllPost() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> new PostListResponse(
                        post.getTitle(),
                        post.getCreatedAt(),
                        post.getPostType().toString()))
                .toList();
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, Long userId) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        if (!post.getUser().getId().equals(userId)) {
            throw NoAuthorException.EXCEPTION; // 권한 없음 예외
        }

        postRepository.deleteById(id);
    }
}