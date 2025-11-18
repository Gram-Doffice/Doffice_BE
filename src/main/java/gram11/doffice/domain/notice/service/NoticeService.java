package gram11.doffice.domain.notice.service;

import gram11.doffice.domain.image.service.NoticeImageService;
import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.responseDto.ResponseNoticeAllDto;
import gram11.doffice.domain.notice.dto.responseDto.ResponseNoticeDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.repository.NoticeRepository;
import gram11.doffice.domain.notice.dto.responseDto.ResponseUserDto;
import gram11.doffice.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(NotFoundException::new);

        // 이미지 삭제 로직
        if (notice.getNoticeImages() != null && !notice.getNoticeImages().isEmpty()) {
            noticeImageService.deleteImages(notice.getNoticeImages());
        }

        // 공지글 삭제
        noticeRepository.delete(notice);
    }

    // 공지글 상세 조회
    @Transactional(readOnly = true)
    public ResponseNoticeDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        ResponseUserDto userDto = new ResponseUserDto(
                notice.getUser().getId(),
                notice.getUser().getUsername()
        );

        List<ResponseNoticeDto.ImageDto> imageDtos = notice.getNoticeImages().stream()
                .map(image -> ResponseNoticeDto.ImageDto.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return ResponseNoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .user(userDto)
                .images(imageDtos)
                .build();
    }

    // 공지글 전체 조회
    @Transactional(readOnly = true)
    public ResponseNoticeAllDto getAllNotice() {
        List<Notice> notices = noticeRepository.findAll();

        List<ResponseNoticeAllDto.NoticeDto> noticeDtos = notices.stream()
                .map(notice -> ResponseNoticeAllDto.NoticeDto.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .build())
                .collect(Collectors.toList());

        // Builder를 사용하여 ResponseNoticeAllDto 생성
        return ResponseNoticeAllDto.builder()
                .notice(noticeDtos)
                .build();
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long id, UpdateNoticeDto request) throws IOException {

        // notice id 가져오기
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 기존 이미지 삭제
        if (notice.getNoticeImages() != null && !notice.getNoticeImages().isEmpty()) {
            noticeImageService.deleteImages(notice.getNoticeImages());
            notice.getNoticeImages().clear();
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