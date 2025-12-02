package gram11.doffice.domain.lost.service;

import gram11.doffice.domain.image.entity.LostImage;
import gram11.doffice.domain.image.service.LostImageService;
import gram11.doffice.domain.lost.dto.requestDto.RequestLostDto;
import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.lost.repository.LostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LostService {
    private final LostRepository lostRepository;
    private final LostImageService lostImageService;

    // 분실물 작성
    @Transactional
    public void createLost(RequestLostDto requestLostDto, List<MultipartFile> files) throws IOException {
        // Lost 엔티티 생성&저장
        Lost lost = Lost.builder()
                .title(requestLostDto.getTitle())
                .content(requestLostDto.getContent())
                .build();
        Lost savedLost = lostRepository.save(lost); // 먼저 저장해야 Lost ID

        // 이미지 파일 저장 & Lost 엔티티에 연결
        if (files != null && !files.isEmpty()) {
            lostImageService.saveImages(files, savedLost.getId());
        }
    }

    // 분실물 수정
    @Transactional
    public void updateLost(Long lostId, RequestLostDto requestLostDto,
                           List<MultipartFile> newFiles, List<Long> deletedImageIds) throws IOException {
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new RuntimeException("분실물 없음"));

        // 업데이트
        lost.updateLost(requestLostDto.getTitle(), requestLostDto.getContent());

        //기존 이미지 삭제
        if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
            // 삭제할 LostImage 객체들
            List<LostImage> imagesToDelete = lost.getLostImages().stream()
                    .filter(img -> deletedImageIds.contains(img.getId()))
                    .collect(Collectors.toList());

            //이미지 삭제
            lostImageService.deleteImages(imagesToDelete);
            lost.getLostImages().removeAll(imagesToDelete);
        }
        // 새 이미지 추가
        if (newFiles != null && !newFiles.isEmpty()) {
            lostImageService.saveImages(newFiles, lostId);
        }
    }

    @Transactional
    public void deleteLost(Long lostId) {
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new RuntimeException("분실물 없음"));

        // 1.  Lost에 연결된 이미지들 조회
        List<LostImage> lostImages = lost.getLostImages();

        // 2. 로컬 파일 시스템에서 이미지 파일 삭제
        lostImageService.deleteImages(lostImages);

        // 3. Lost 엔티티 삭제
        lostRepository.delete(lost);
    }

    public Lost getLost(Long id) {
        return lostRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
    }

    public List<Lost> getAllLost() {
        return lostRepository.findAll();
    }
}