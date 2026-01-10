package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.post.exception.NoAuthorException;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.exception.WrongPostTypeException;
import gram11.doffice.domain.post.presentaton.dto.request.CreateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import gram11.doffice.domain.user.domain.User;
import gram11.doffice.domain.user.domain.repository.UserRepository;
import gram11.doffice.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PostListResponse> filterNotice(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByPostType(PostType.NOTICE, pageable);

        return posts.stream()
                .map(post -> PostListResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .createAt(post.getCreatedAt())
                        .type(post.getPostType().toString())
                        .build())
                .toList();
    }

    // 공지사항 작성
    @Transactional
    public void createNotice(CreateNoticeRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .postType(PostType.NOTICE)
                .imageKey(null)
                .user(user)
                .build();
        postRepository.save(post);
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeRequest request, Long userId) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        if (post.getPostType() != PostType.NOTICE) {
            throw WrongPostTypeException.EXCEPTION;
        }

        if (!post.getUser().getId().equals(userId)) {
            throw NoAuthorException.EXCEPTION; // 권한 없음 예외
        }

        post.updatePost(request.title(), request.content(), null);
    }
}
