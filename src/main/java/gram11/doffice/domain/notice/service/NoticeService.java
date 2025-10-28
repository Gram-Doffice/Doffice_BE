package gram11.doffice.domain.notice.service;

import gram11.doffice.domain.notice.dto.requestDto.CreateNoticeDto;
import gram11.doffice.domain.notice.dto.requestDto.UpdateNoticeDto;
import gram11.doffice.domain.notice.entity.Notice;
import gram11.doffice.domain.notice.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public void createNotice(CreateNoticeDto noticeDto) {
        Notice notice = new Notice();
        notice.updateNotice(noticeDto.getTitle(), noticeDto.getContent());
        noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
    }

    public void updateNotice(Long id, UpdateNoticeDto updateNoticeDto) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
        notice.updateNotice(updateNoticeDto.getTitle(), updateNoticeDto.getContent());
        noticeRepository.save(notice);
    }
}
