package gram11.doffice.domain.image.entity;

import gram11.doffice.domain.post.domain.Post;
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
    @JoinColumn(name = "notice_id", nullable = false)
    private Post post;

    // Notice 관계 설정용 메서드
    public void connectNotice(Post post) {
        this.post = post;
    }

    public static Image createOfNotice(String imageUrl, Post post) {
        Image image = new Image();
        image.imageUrl = imageUrl;
        image.connectNotice(post);
        return image;
    }
}