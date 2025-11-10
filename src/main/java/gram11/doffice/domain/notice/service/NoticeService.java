package gram11.doffice.domain.notice.service;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.image.service.NoticeImageService;
import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UploadImageDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeImageService noticeImageService;

    // 공지사항 작성
    @Transactional
    public void createNotice(CreateNoticeDto noticeDto, UploadImageDto uploadImageDto) throws IOException {
        Notice notice = new Notice();
        notice.updateNotice(noticeDto.getTitle(), noticeDto.getContent());

        if (uploadImageDto != null && uploadImageDto.getImages() != null && !uploadImageDto.getImages().isEmpty()) {
            noticeImageService.saveImages(uploadImageDto.getImages(), notice);
        }

        noticeRepository.save(notice);
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    // 공지글 상세 조회
    @Transactional
    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
    }

    // 공지글 전체 조회
    @Transactional
    public List<Notice> getAllNotice() {
        return noticeRepository.findAll();
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeDto updateNoticeDto) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
        notice.updateNotice(updateNoticeDto.getTitle(), updateNoticeDto.getContent());
        noticeRepository.save(notice);
    }
}