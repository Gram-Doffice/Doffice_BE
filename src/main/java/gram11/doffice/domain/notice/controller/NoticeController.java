package gram11.doffice.domain.notice.controller;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notice")
@RestController
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotice(@RequestBody CreateNoticeDto noticeDto) {
        noticeService.createNotice(noticeDto);
    }

    @DeleteMapping("/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable("notice_id") Long parameter) {
        noticeService.deleteNotice(parameter);
    }

    @GetMapping("/{notice_id}")
    public Notice getNotice(@PathVariable("notice_id") Long parameter) {
        return noticeService.getNotice(parameter);
    }

    @PutMapping("/{notice_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNotice(@PathVariable("notice_id") Long parameter, @RequestBody UpdateNoticeDto noticeDto) {
        noticeService.updateNotice(parameter, noticeDto);
    }
}
