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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
        if (!notice.getImages().contains(this)) {
            notice.getImages().add(this);
        }
    }

//    public void setLostPost(LostPost lostPost) {
//        this.lostPost = lostPost;
//        if (!lostPost.getImages().contains(this)) {
//            lostPost.getImages().add(this);
//        }
//    }
}
