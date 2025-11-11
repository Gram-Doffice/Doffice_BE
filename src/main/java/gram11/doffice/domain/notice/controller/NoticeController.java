package gram11.doffice.domain.notice.controller;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UploadImageDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/notice")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createNotice(
            @RequestPart CreateNoticeDto noticeDto,
            @RequestPart(required = false) UploadImageDto uploadImageDto)
            throws IOException {
        noticeService.createNotice(noticeDto, uploadImageDto);
    }

    // 공지글 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{notice_id}")
    public void deleteNotice(@PathVariable("notice_id") Long parameter) {
        noticeService.deleteNotice(parameter);
    }

    // 공지글 상세 조회
    @GetMapping("/{notice_id}")
    public Notice getNotice(@PathVariable("notice_id") Long parameter) {
        return noticeService.getNotice(parameter);
    }

    // 전체 공지사항 조회
    @GetMapping
    public List<Notice> getAllNotice() {
        return noticeService.getAllNotice();
    }

    // 공지글 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{notice_id}")
    public void updateNotice(
            @PathVariable("notice_id") Long parameter,
            @RequestPart UpdateNoticeDto noticeDto,
            @RequestPart(required = false) UploadImageDto uploadImageDto)
            throws IOException {

        noticeService.updateNotice(parameter, noticeDto, uploadImageDto);
    }
}
