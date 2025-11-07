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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    private LostPost lostPost;

    public static Image createNotice(String imageUrl, Notice notice) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.notice = notice;
        return image;
    }

//    public static Image createLostPost(String imageUrl, LostPost lostPost) {
//        Image image = new Image();
//        image.imageUrl = imageUrl;
//        image.lostPost = lostPost;
//        return image;
//    }
}