package gram11.doffice.domain.image.entity;

import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // Notice 관계 설정용 메서드
    public void connectNotice(Notice notice) {
        this.notice = notice;
    }

    public static Image createNotice(String imageUrl, Notice notice) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.notice = notice;
        return image;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    private Lost lost;

    public void connectLost(Lost lost) {
        this.lost = lost;
    }

    public static Image createLost(String imageUrl, Lost lost) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.lost = lost;
        return image;
    }

}