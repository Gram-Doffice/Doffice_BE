package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.repository.ImageRepository;
import gram11.doffice.domain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeImageService {

    private final ImageRepository imageRepository;
    private final FileProperties fileProperties;

    public List<Image> saveImages (List<MultipartFile> files, Notice notice) throws Exception {
        List<Image> images = new ArrayList<>();

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
            Image image = Image.createNotice(filePath, notice);
            imageRepository.save(image);
        }
        return images;
    }
}
