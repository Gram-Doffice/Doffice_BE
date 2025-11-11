package gram11.doffice.domain.lost.entity;

import gram11.doffice.domain.image.entity.Image;
import gram11.doffice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class LostPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User user;

    // cascade = CascadeType.ALL, orphanRemoval = true 를 유지하여 이미지 삭제 시 연관된 Image DB 레코드도 삭제되도록 함.
    @OneToMany(mappedBy = "lostPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public LostPost(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    @CreatedDate
    @Column(updatable = false) // DB에서 수정되지 않도록 설정
    private LocalDateTime createdAt;

    // 글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 이미지 추가 (Image 엔티티에 연결 로직을 둠: addImage(Image image) -> image.connectLostPost(this))
    public void addImage(Image image) {
        images.add(image);

        // 여기서 호출하도록 둘 수 있습니다. Image 엔티티의 구조를 따름
        image.connectLostPost(this); // Image.createLostPost에서 이미 처리되었을 수 있으나, 안전을 위해 유지
    }

    // 이미지 삭제
    public void removeImage(Image image) {
        images.remove(image);
        // Image 엔티티에서 LostPost 참조를 끊어줌
        image.disconnectLostPost();
    }
}