package gram11.doffice.domain.lost.entity;

<<<<<<< Updated upstream
import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.user.entity.User; // ⭐ 추가
import jakarta.persistence.*;
=======
import gram11.doffice.domain.user.entity.User;
import jakarta.persistence.*;

>>>>>>> Stashed changes
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class LostPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

<<<<<<< Updated upstream
    // ⭐ 추가: User (작성자) 관계 - manager_id DB 오류 해결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "lostPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    // ⭐ 필수: 모든 필수 필드를 받는 유일한 생성자
    public LostPost(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 이미지 추가
    public void addImage(Image image) {
        images.add(image);
    }

    // 이미지 삭제 시 연관관계 해제 로직 추가
    public void removeImage(Image image) {
        images.remove(image);
        image.disconnectLostPost(); // ⭐ Image 엔티티 메서드 호출
    }
}
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User user;
}
>>>>>>> Stashed changes
