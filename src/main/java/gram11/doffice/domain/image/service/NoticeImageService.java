package gram11.doffice.domain.image.service;

import gram11.doffice.domain.image.entity.NoticeImage;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.repository.NoticeRepository;
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

    private final NoticeRepository noticeRepository;
    private final FileProperties fileProperties;

    public void saveImages (List<MultipartFile> files, Long noticeId) throws IOException {
        List<NoticeImage> noticeImages = new ArrayList<>();

        // DB에서 notice 조회
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        for (MultipartFile file : files){
            if (file.isEmpty()) throw new RuntimeException("파일이 비어있습니다.");

            String originalName = file.getOriginalFilename();

            if (originalName == null || !originalName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                throw new RuntimeException("허용되지 않는 파일 형식입니다.");
            }

            // 파일명, 경로 설정
            String safeFileName = originalName.replaceAll("[\\\\/:*?\"<>|;]", "_");
            String fileName = UUID.randomUUID() + "_" + safeFileName;
            String filePath = Paths.get(fileProperties.getUploadDir(), fileName).toString();

            // 로컬에 저장
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);

            // DB에 저장할 URL
            String imageUrl = "/images/notice/" + fileName;
            NoticeImage noticeImage = NoticeImage.createOfNotice(imageUrl, notice);

            notice.addImage(noticeImage);
        }
    }

    public void deleteImages(List<NoticeImage> noticeImages) {
        for (NoticeImage noticeImage : noticeImages) {

            // DB에 저장된 URL -> 로컬 경로 계산
            String fileName = Paths.get(noticeImage.getImageUrl()).getFileName().toString();
            File file = new File(Paths.get(fileProperties.getUploadDir(), fileName).toString());

            if (file.exists()) {
                file.delete();
            }
        }
    }
}
