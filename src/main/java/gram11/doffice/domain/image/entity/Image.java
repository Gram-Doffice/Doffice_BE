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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // Notice 관계 설정용 메서드
    public void connectNotice(Notice notice) {
        this.notice = notice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    private LostPost lostPost;

    public static Image createNotice(String imageUrl, Notice notice) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.notice = notice;
        return image;
    }

    // LostPost 관련 메서드
    // 1. LostPost 관계 설정 (LostPostImageService에서 사용)
    public void connectLostPost(LostPost lostPost) {
        this.lostPost = lostPost;
    }

    // 2. LostPost 연관관계 해제 (LostPost.removeImage에서 에러가 난 핵심 메서드)
    public void disconnectLostPost() {
        this.lostPost = null;
    }

    // 3. LostPost용 이미지 생성 (LostPostImageService에서 사용)
    public static Image createLostPost(String imageUrl, LostPost lostPost) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        // 위에서 추가한 connectLostPost 메서드를 사용하도록 구현 변경
        image.connectLostPost(lostPost);
        return image;
    }

}