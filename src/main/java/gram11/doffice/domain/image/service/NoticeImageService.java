package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.repository.ImageRepository;
import gram11.doffice.domain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeImageService {

    private final ImageRepository imageRepository;
    private final FileProperties fileProperties;

    public List<Image> saveImages (List<MultipartFile> files, Notice notice) throws IOException {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files){
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                continue; // 또는 예외 던지기
            }

            // 파일명, 경로 설정
            String fileName = UUID.randomUUID() + "_" + originalName;
            String filePath = Paths.get(fileProperties.getUploadDir(), fileName).toString();

            // 로컬에 저장
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);

            // DB에 저장할 URL
            String imageUrl = "/images/notice/" + fileName;
            Image image = Image.createOfNotice(imageUrl, notice);
            imageRepository.save(image);


            images.add(image);
        }
        return images;
    }

    public void deleteImages(List<Image> images) {
        for (Image image : images) {

            // DB에 저장된 URL -> 로컬 경로 계산
            String fileName = Paths.get(image.getImageUrl()).getFileName().toString();
            File file = new File(Paths.get(fileProperties.getUploadDir(), fileName).toString());

            if (file.exists()) {
                file.delete();
            }

            // DB에서 삭제
            imageRepository.delete(image);
        }
    }

}
