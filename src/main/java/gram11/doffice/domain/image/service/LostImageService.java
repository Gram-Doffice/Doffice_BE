package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.LostImage;
import gram11.doffice.domain.image.repository.ImageRepository;
import gram11.doffice.domain.lost.entity.Lost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LostImageService {

    private final ImageRepository imageRepository;
    private final FileProperties fileProperties;

    public List<LostImage> saveImages (List<MultipartFile> files, Lost lost) throws Exception {
        List<LostImage> lostImages = new ArrayList<>();

        for (MultipartFile file : files){
            if (file.isEmpty()) continue;

            // 파일명, 경로 설정
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = fileProperties.getUploadDir() + fileName;

            // 로컬에 저장
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);

            // DB에 저장
            LostImage lostImage = LostImage.createLost(filePath, lost);
            imageRepository.save(lostImage);
            lostImages.add(lostImage);
        }

        return lostImages;
    }

    public LostImage getImage(Long imageId) {
        // 이미지가 존재하지 않을 경우 예외 처리 필요
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("이미지가 없습니다."));
    }


    @Transactional
    public void deleteImage(Long imageId) {
        LostImage lostImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("이미지가 없습니다."));

        // 1. 로컬 파일 삭제 (실제 파일 경로 사용)
        File file = new File(lostImage.getImageUrl());
        if (file.exists()) {
            file.delete();
        }

        // 2. DB 기록 삭제
        imageRepository.delete(lostImage);
    }
}