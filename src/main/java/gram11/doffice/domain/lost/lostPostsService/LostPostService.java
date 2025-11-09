package gram11.doffice.domain.lost.service;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.repository.LostPostRepository;
import gram11.doffice.domain.lost.dto.requestDto.AddImageDto;
import gram11.doffice.domain.lost.dto.requestDto.LostPostRequestDto; // ⭐ 추가
import gram11.doffice.domain.user.entity.User; // ⭐ 추가
import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.service.LostPostImageService; // ⭐ 추가 (책임 분리)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException; // ⭐ 추가

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LostPostService {

    private final LostPostRepository lostPostRepository;
    private final LostPostImageService lostPostImageService; // ⭐ 주입

    public List<LostPost> getAllLostPosts() {
        return lostPostRepository.findAll();
    }

    public Optional<LostPost> getLostPostById(Long id) {
        return lostPostRepository.findById(id);
    }

    // ⭐ 수정: DTO와 User 객체를 받아 엔티티를 생성하고 저장하도록 변경
    public LostPost createLostPost(LostPostRequestDto dto, User user) {
        LostPost post = new LostPost(dto.getTitle(), dto.getContent(), user);
        return lostPostRepository.save(post);
    }

    @Transactional
    public LostPost updateLostPost(Long id, String title, String content) {
        LostPost post = lostPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));
        post.update(title, content);
        return lostPostRepository.save(post);
    }

    public void deleteLostPost(Long id) {
        lostPostRepository.deleteById(id);
    }

    // ⭐ 수정: 이미지 저장 책임을 LostPostImageService에 위임하여 안정성 확보
    @Transactional
    public LostPost addImage(Long postId, AddImageDto dto) {
        LostPost post = lostPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));

        // LostPostImageService를 통해 Image를 DB에 저장하고 엔티티를 받아옴
        Image image = lostPostImageService.addImageToLostPost(dto.getImageUrl(), post);

        // LostPost 엔티티의 컬렉션에도 Image 추가
        post.addImage(image);

        return post;
    }

    // ⭐ 수정: removeImage 로직 개선
    @Transactional
    public LostPost removeImage(Long postId, Long imageId) {
        LostPost post = lostPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));

        Image imageToRemove = post.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 이미지를 찾을 수 없습니다."));

        // LostPost 엔티티의 removeImage 메서드를 사용하여 연관관계 해제 및 리스트에서 제거
        post.removeImage(imageToRemove);

        return post;
    }
}