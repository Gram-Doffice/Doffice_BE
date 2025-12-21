package gram11.doffice.domain.post.service;

import gram11.doffice.domain.post.domain.Post;
import gram11.doffice.domain.post.domain.repository.PostRepository;
import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.post.exception.ImageExceededException;
import gram11.doffice.domain.post.exception.NoAuthorException;
import gram11.doffice.domain.post.exception.PostNotFoundException;
import gram11.doffice.domain.post.presentaton.dto.request.CreateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import gram11.doffice.domain.user.domain.User;
import gram11.doffice.domain.user.domain.repository.UserRepository;
import gram11.doffice.global.s3.S3BucketFolder;
import gram11.doffice.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;

    // 분실물 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponse> filterLost() {
        List<Post> posts = postRepository.findByPostType(PostType.LOST);

        return posts.stream()
                .map(post -> PostListResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .createAt(post.getCreatedAt())
                        .type(post.getPostType().toString())
                        .build())
                .toList();
    }

    // 분실물 게시글 작성
    @Transactional
    public void createLost(CreateLostRequest request, List<MultipartFile> images, Long userId) {
        validateImageCount(images.size());

        // 이미지 업로드
        List<String> imagesUrl = uploadImage(images);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .imageKey(imagesUrl)
                .postType(PostType.LOST)
                .build();
        postRepository.save(post);
    }

    // 분실물 게시글 수정
    @Transactional
    public void updateLost(Long id, UpdateLostRequest request, List<MultipartFile> images, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> PostNotFoundException.EXCEPTION);

        // 권한 없음 예외
        if (!post.getUser().getId().equals(userId)) {
            throw NoAuthorException.EXCEPTION;
        }

        // 이미지 개수 검사
        int totalSize = request.keepImagesUrl().size() + ((images != null) ? images.size() : 0);
        validateImageCount(totalSize);

        // S3 업로드
        List<String> newImagesUrl = uploadImage(images);

        // DB에 저장
        List<String> updatedImages = new ArrayList<>(request.keepImagesUrl());
        updatedImages.addAll(newImagesUrl);
        post.updatePost(request.title(), request.content(), updatedImages);

        // 삭제 로직 (기존 이미지 중 유지 목록에 없는 것들)
        List<String> currentImages = post.getImageKey();
        currentImages.stream()
                .filter(url -> !request.keepImagesUrl().contains(url))
                .forEach(s3UploadService::delete); // S3에서 실제 파일 삭제
    }

    private List<String> uploadImage(List<MultipartFile> images){
        // 이미지가 없을 경우 빈 리스트 리턴
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        // 이미지 업로드 및 생성된 리스트 리턴
        return images.stream()
                .filter(file -> !file.isEmpty())
                .map(file -> s3UploadService.upload(file, S3BucketFolder.LOST.getPath()))
                .collect(Collectors.toList());
    }

    private void validateImageCount(int size) {
        if (size > 3) {
            throw ImageExceededException.EXCEPTION;
        }
    }
}
