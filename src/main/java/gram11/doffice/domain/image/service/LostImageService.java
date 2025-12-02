package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.LostImage;
import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.lost.repository.LostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LostImageService {

    private final LostRepository lostRepository;
    private final FileProperties fileProperties;

    @Transactional
    //이미지 저장
    public void saveImages (List<MultipartFile> files, Long lostid) throws IOException {

        Lost lost = lostRepository.findById(lostid)
                .orElseThrow(() -> new RuntimeException("분실물 없음"));

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName == null) continue;

            // 파일명, 경로 설정
            String safeFileName = originalName.replaceAll("[\\\\/:*?\"<>|;]", "_");
            String fileName = UUID.randomUUID() + "_" + originalName;
            String filePath = Paths.get(fileProperties.getUploadDir(), fileName).toString();

            // 로컬에 저장
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);

            // DB에 저장할 URL
            String imageUrl = "/images/lost/" + fileName;
            LostImage lostImage = LostImage.createLost(imageUrl, lost);

            lost.addLostImage(lostImage);
        }
    }

    public void deleteImages(List<LostImage> lostImages) {
        for (LostImage lostImage : lostImages) {

            // DB에 저장된 URL -> 로컬 경로 계산
            String fileName = Paths.get(lostImage.getImageUrl()).getFileName().toString();
            File file = new File(Paths.get(fileProperties.getUploadDir(), fileName).toString());

            if (file.exists()) {
                file.delete();
            }
        }
    }
}