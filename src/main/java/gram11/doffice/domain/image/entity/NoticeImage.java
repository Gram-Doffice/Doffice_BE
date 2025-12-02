package gram11.doffice.domain.image.entity;

import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    // Notice 관계 설정용 메서드
    public void connectNotice(Notice notice) {
        this.notice = notice;
    }

    public static NoticeImage createOfNotice(String imageUrl, Notice notice) {
        NoticeImage noticeImage = new NoticeImage();
        noticeImage.imageUrl = imageUrl;
        noticeImage.connectNotice(notice);
        return noticeImage;
    }
}