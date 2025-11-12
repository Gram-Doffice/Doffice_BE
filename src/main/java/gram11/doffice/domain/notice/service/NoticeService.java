package gram11.doffice.domain.notice.service;

import gram11.doffice.domain.image.service.NoticeImageService;
import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.responseDto.ResponseNoticeDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.repository.NoticeRepository;
import gram11.doffice.domain.user.dto.ResponseUserDto;
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
    public void createNotice(CreateNoticeDto request) throws IOException {
        Notice notice = new Notice();
        notice.updateNotice(request.getTitle(), request.getContent());

        noticeRepository.save(notice);

        // 이미지 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            noticeImageService.saveImages(request.getImages(), notice.getId());
        }
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        // 이미지 삭제 로직
        if (notice.getImages() != null && !notice.getImages().isEmpty()) {
            noticeImageService.deleteImages(notice.getImages());
        }

        // 공지글 삭제
        noticeRepository.delete(notice);
    }

    // 공지글 상세 조회
    @Transactional
    public ResponseNoticeDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        ResponseUserDto userDto = new ResponseUserDto(
                notice.getUser().getId(),
                notice.getUser().getUsername()
        );

        return new ResponseNoticeDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                userDto
        );
    }

    // 공지글 전체 조회
    // TODO dto 이용하도록 수정
    @Transactional
    public List<Notice> getAllNotice() {
        return noticeRepository.findAll();
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeDto request) throws IOException {

        // notice id 가져오기
        Notice notice = noticeRepository.findById(id).orElseThrow(()
                -> new RuntimeException("존재하지 않는 게시글입니다."));

        // 기존 이미지 삭제
        if (notice.getImages() != null && !notice.getImages().isEmpty()) {
            noticeImageService.deleteImages(notice.getImages());
            notice.getImages().clear();
        }

        // 이미지 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            noticeImageService.saveImages(request.getImages(), notice.getId());
        }

        // 게시글 수정사항 저장
        notice.updateNotice(request.getTitle(), request.getContent());
        noticeRepository.save(notice);
    }
}