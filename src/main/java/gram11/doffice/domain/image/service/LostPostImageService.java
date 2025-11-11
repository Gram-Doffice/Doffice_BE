package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.repository.ImageRepository;
import gram11.doffice.domain.lost.entity.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LostPostImageService {

    private final ImageRepository imageRepository;
    private final FileProperties fileProperties; // NoticeImageService와 동일하게 사용

    /**
     * LostPost에 연결된 이미지 파일들을 로컬에 저장하고 DB에 Image 엔티티를 저장합니다.
     * 기능은 NoticeImageService의 saveImages와 동일합니다.
     */
    @Transactional
    public List<Image> saveImages (List<MultipartFile> files, LostPost post) throws Exception {
        List<Image> savedImages = new ArrayList<>();

        for (MultipartFile file : files){
            if (file.isEmpty()) continue;

            // 파일명, 경로 설정
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = fileProperties.getUploadDir() + fileName;

            // 로컬에 저장 (파일 업로드)
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);

            // DB에 저장
            Image image = Image.createLostPost(filePath, post);
            Image savedImage = imageRepository.save(image);

            // LostPost 엔티티의 이미지 리스트에 추가 (LostPost 엔티티 내부 로직 사용)
            post.addImage(savedImage);
            savedImages.add(savedImage);
        }
        // **NoticeService와 달리, LostPostService에서 post.addImage(savedImage)를 호출하지 않고
        // 여기서 처리하도록 변경했으므로 List를 반환합니다.**
        return savedImages;
    }

    /**
     * 특정 Image ID로 Image 엔티티를 찾아오는 책임
     */
    public Image findImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 이미지를 찾을 수 없습니다."));
    }

    /**
     * Image 엔티티를 DB에서 삭제하는 책임 (로컬 파일 삭제 로직은 생략)
     */
    @Transactional
    public void deleteImageById(Long imageId) {
        imageRepository.deleteById(imageId);
    }

}