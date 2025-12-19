package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.post.exception.NoAuthorException;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.presentaton.dto.request.CreateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostListResponse> filterNotice() {
        List<Post> posts = postRepository.findByPostType(PostType.NOTICE);

        return posts.stream()
                .map(post -> new PostListResponse(
                        post.getTitle(),
                        post.getCreatedAt(),
                        post.getPostType().toString()))
                .toList();
    }

    // 공지사항 작성
    @Transactional
    public void createNotice(CreateNoticeRequest request) {
        Post post = new Post(request.title(), request.content(), PostType.NOTICE, null);
        postRepository.save(post);
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeRequest request, Long userId) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        if (!post.getUser().getId().equals(userId)) {
            throw NoAuthorException.EXCEPTION; // 권한 없음 예외
        }

        post.updatePost(request.title(), request.content(), null);
    }
}
