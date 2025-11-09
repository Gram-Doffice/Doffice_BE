package gram11.doffice.domain.notice.controller;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/notice")
@RestController
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // 공지글 작성
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotice(@RequestBody CreateNoticeDto noticeDto) {
        noticeService.createNotice(noticeDto);
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
