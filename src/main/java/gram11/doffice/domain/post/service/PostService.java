package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.post.presentaton.dto.response.PostDetailResponse;
import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        return new PostDetailResponse(
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                PostType.NOTICE.toString()
        );
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
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw PostNotFoundException.EXCEPTION;
        }

        postRepository.deleteById(id);
    }
}