package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.presentaton.dto.request.CreateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LostService {

    private final PostRepository postRepository;

    // 분실물 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponse> filterLost() {
        List<Post> posts = postRepository.findByPostType(PostType.LOST);

        return posts.stream()
                .map(post -> new PostListResponse(
                        post.getTitle(),
                        post.getCreatedAt(),
                        post.getPostType().toString()))
                .toList();
    }

    // 분실물 게시글 작성
    @Transactional
    public void createLost(CreateLostRequest request) {
        Post post = new Post(request.title(), request.content(), PostType.NOTICE);
        postRepository.save(post);
    }

    // 분실물 게시글 수정
    @Transactional
    public void updateLost(Long id, UpdateLostRequest request) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        post.updatePost(request.title(), request.content());
    }
}
