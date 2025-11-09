package gram11.doffice.domain.lost.service;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.repository.LostPostRepository;
import gram11.doffice.domain.lost.dto.requestDto.AddImageDto;
import gram11.doffice.domain.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LostPostService {

    private final LostPostRepository lostPostRepository;

    public List<LostPost> getAllLostPosts() {
        return lostPostRepository.findAll();
    }

    public Optional<LostPost> getLostPostById(Long id) {
        return lostPostRepository.findById(id);
    }

    public LostPost createLostPost(LostPost post) {
        return lostPostRepository.save(post);
    }

    public LostPost updateLostPost(Long id, String title, String content) {
        LostPost post = lostPostRepository.findById(id).orElseThrow();
        post.update(title, content);
        return lostPostRepository.save(post);
    }

    public void deleteLostPost(Long id) {
        lostPostRepository.deleteById(id);
    }

    @Transactional
    public LostPost addImage(Long postId, AddImageDto dto) {
        LostPost post = lostPostRepository.findById(postId).orElseThrow();
        Image image = Image.createLostPost(dto.getImageUrl(), post);
        post.addImage(image);
        return lostPostRepository.save(post);
    }

    @Transactional
    public LostPost removeImage(Long postId, Long imageId) {
        LostPost post = lostPostRepository.findById(postId).orElseThrow();
        post.getImages().removeIf(img -> img.getId().equals(imageId));
        return lostPostRepository.save(post);
    }
}
