package gram11.doffice.domain.notice.controller;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UploadImageDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/notice")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지글 작성
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createNotice(
            @RequestPart CreateNoticeDto noticeDto,
            @RequestPart(required = false) UploadImageDto uploadImageDto) {
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
    public ResponseEntity<?> deleteNotice(@PathVariable("notice_id") Long parameter) {
        try {
            noticeService.deleteNotice(parameter);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 중 오류가 발생했습니다.");
        }
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
    public ResponseEntity<String> updateNotice(
            @PathVariable("notice_id") Long parameter,
            @RequestPart UpdateNoticeDto noticeDto,
            @RequestPart(required = false) UploadImageDto uploadImageDto) {
        try {
            noticeService.updateNotice(parameter, noticeDto, uploadImageDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("공지사항 수정 완료");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 저장 실패");
        }
    }
}
