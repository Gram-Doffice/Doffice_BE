package gram11.doffice.domain.image.entity;

import gram11.doffice.domain.lost.entity.LostPost;
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

    // Notice와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // LostPost와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    private LostPost lostPost;

    // Notice 관계 설정
    public void connectNotice(Notice notice) {
        this.notice = notice;
    }

    // LostPost 관계 설정
    public void connectLostPost(LostPost lostPost) {
        this.lostPost = lostPost;
    }

    // Notice용 이미지 생성
    public static Image createNotice(String imageUrl, Notice notice) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.connectNotice(notice);
        return image;
    }

    // LostPost용 이미지 생성
    public static Image createLostPost(String imageUrl, LostPost lostPost) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.connectLostPost(lostPost);
        return image;
    }
}
