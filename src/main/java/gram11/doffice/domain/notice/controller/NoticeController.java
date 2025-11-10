package gram11.doffice.domain.notice.controller;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UploadImageDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/notice")
@RestController
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // 공지글 작성
    @PostMapping("/post")
    public ResponseEntity<String> createNotice(@RequestBody CreateNoticeDto noticeDto, @RequestPart UploadImageDto uploadImageDto) {
        try {
            noticeService.createNotice(noticeDto, uploadImageDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("공지사항 작성 완료");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 저장 실패");
        }
    }

    // 공지글 삭제
    @DeleteMapping("/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    @PutMapping("/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNotice(@PathVariable("notice_id") Long parameter, @RequestBody UpdateNoticeDto noticeDto) {
        noticeService.updateNotice(parameter, noticeDto);
    }
}
