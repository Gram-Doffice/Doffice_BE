package gram11.doffice.domain.post.domain;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.user.entity.User;
import gram11.doffice.global.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Post extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 이미지 추가
    public void addImage(Image image) {
        images.add(image);
        image.connectNotice(this);
    }
}
