package gram11.doffice.domain.post.service;

import gram11.doffice.domain.image.service.ImageService;
import gram11.doffice.domain.post.presentaton.dto.request.CreateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostResponse;
import gram11.doffice.domain.post.presentaton.dto.response.PostDetailResponse;
import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.presentaton.dto.response.UserResponse;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;

    // 공지사항 작성
    @Transactional
    public void createNotice(CreateNoticeRequest request) throws IOException {
        Post post = new Post();
        post.updateNotice(request.getTitle(), request.getContent());

        postRepository.save(post);

        // 이미지 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            imageService.saveImages(request.getImages(), post.getId());
        }
    }

    // 공지사항 작성
    @Transactional
    public void createLost(CreateNoticeRequest request) throws IOException {
        Post post = new Post();
        post.updateNotice(request.getTitle(), request.getContent());

        postRepository.save(post);

        // 이미지 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            imageService.saveImages(request.getImages(), post.getId());
        }
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        // 이미지 삭제 로직
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            imageService.deleteImages(post.getImages());
        }

        // 공지글 삭제
        postRepository.delete(post);
    }

    // 공지글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailResponse getNotice(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        UserResponse userDto = new UserResponse(
                post.getUser().getId(),
                post.getUser().getUsername()
        );

        List<PostDetailResponse.ImageDto> imageDtos = post.getImages().stream()
                .map(image -> PostDetailResponse.ImageDto.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .user(userDto)
                .images(imageDtos)
                .build();
    }

    // 공지글 전체 조회
    @Transactional(readOnly = true)
    public PostResponse getAllNotice() {
        List<Post> posts = postRepository.findAll();

        List<PostResponse.NoticeDto> noticeDtos = posts.stream()
                .map(post -> PostResponse.NoticeDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .collect(Collectors.toList());

        // Builder를 사용하여 ResponseNoticeAllDto 생성
        return PostResponse.builder()
                .notice(noticeDtos)
                .build();
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeRequest request) throws IOException {

        // notice id 가져오기
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        // 기존 이미지 삭제
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            imageService.deleteImages(post.getImages());
            post.getImages().clear();
        }

        // 이미지 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            imageService.saveImages(request.getImages(), post.getId());
        }

        // 게시글 수정사항 저장
        post.updateNotice(request.getTitle(), request.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void updateLost(Long id, UpdateNoticeRequest request) throws IOException {

        // post id 가져오기
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        // 게시글 수정사항 저장
        post.updateNotice(request.getTitle(), request.getContent());
        postRepository.save(post);
    }
}