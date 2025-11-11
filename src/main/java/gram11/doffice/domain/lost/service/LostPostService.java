package gram11.doffice.domain.lost.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.lost.dto.requestDto.LostPostRequestDto;
import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.repository.LostPostRepository;
import gram11.doffice.domain.user.entity.User;
import gram11.doffice.domain.image.service.LostPostImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LostPostService {

    private final LostPostRepository lostPostRepository;
    private final LostPostImageService lostPostImageService;

    public List<LostPost> getAllLostPosts() {
        return lostPostRepository.findAll();
    }

    public Optional<LostPost> getLostPostById(Long id) {
        return lostPostRepository.findById(id);
    }

    // 공지사항 작성과 유사하게, DTO와 사용자 정보만 받아 생성 (이미지 처리는 분리)
    @Transactional
    public LostPost createLostPost(LostPostRequestDto dto, User user, List<MultipartFile> files) throws Exception {
        LostPost post = new LostPost(dto.getTitle(), dto.getContent(), user);
        LostPost savedPost = lostPostRepository.save(post);

        if (files != null && !files.isEmpty()) {
            // 이미지 저장 책임을 LostPostImageService에 위임
            lostPostImageService.saveImages(files, savedPost);
        }
        return savedPost;
    }

    // 공지사항 수정과 유사하게, 내용만 수정
    @Transactional
    public LostPost updateLostPost(Long id, LostPostRequestDto dto) {
        LostPost post = lostPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));

        post.update(dto.getTitle(), dto.getContent());
        // JPA의 변경 감지로 인해 save 호출은 필수는 아니나, 명시적으로 호출해도 무방
        return post;
    }


    // 이미지 추가
    @Transactional
    public LostPost addImages(Long id, List<MultipartFile> files) throws Exception {
        LostPost post = lostPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));

        if (files != null && !files.isEmpty()) {
            lostPostImageService.saveImages(files, post);
        }
        return post;
    }


    // 이미지 삭제 (핵심 수정 부분)
    @Transactional
    public LostPost removeImage(Long postId, Long imageId) {
        LostPost post = lostPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));

        Image image = lostPostImageService.findImageById(imageId);

        // LostPost 엔티티에서 이미지 제거 및 Image 엔티티에서 LostPost 연결 해제
        post.removeImage(image);

        // DB에서 Image 엔티티 삭제
        lostPostImageService.deleteImageById(imageId);

        return post;
    }


    @Transactional
    public void deleteLostPost(Long id) {
        // cascade = CascadeType.ALL, orphanRemoval = true 로 인해 LostPost 삭제 시 연결된 Image도 함께 삭제됨
        lostPostRepository.deleteById(id);
    }
}