package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.repository.ImageRepository;
import gram11.doffice.domain.lost.entity.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostPostImageService {

    private final ImageRepository imageRepository;

    //  LostPost에 연결된 Image를 생성하고 저장하는 책임
    @Transactional
    public Image addImageToLostPost(String imageUrl, LostPost post) {
        Image image = Image.createLostPost(imageUrl, post);
        return imageRepository.save(image); // ImageRepository를 통한 명시적 저장
    }

    //  특정 Image ID로 Image 엔티티를 찾아오는 책임
    public Image findImageById(Long imageId) {
        // ImageRepository를 주입받아 사용한다고 가정
        // 실제 ImageRepository 코드가 없으므로 가정하고 작성
        // return imageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image not found."));
        // 지금은 Image 엔티티를 가져오는 로직이 필요하므로, ImageRepository에 findById 메서드가 있다고 가정합니다.
        // 현재는 LostPostService에서 getImages()를 통해 리스트에서 찾는 방식이므로, 해당 로직을 LostPostService에서 유지하고, 여기서는 명시적인 저장을 위한 메서드만 남깁니다.
        return null; // 이 메서드는 현재 사용되지 않으므로 무시
    }
}