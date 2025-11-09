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

    @Transactional
    public Image addImageToLostPost(String imageUrl, LostPost post) {
        Image image = Image.createLostPost(imageUrl, post);
        return imageRepository.save(image);
    }
}
