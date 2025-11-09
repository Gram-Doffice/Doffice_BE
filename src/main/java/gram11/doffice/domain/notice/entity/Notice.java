package gram11.doffice.domain.notice.entity;

<<<<<<< Updated upstream
import gram11.doffice.domain.image.entity.Image;
=======
>>>>>>> Stashed changes
import gram11.doffice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String title;

    @Column(columnDefinition = "varchar(2000)")
    private String content;

<<<<<<< Updated upstream
    @OneToMany()
    private List<Image> images = new ArrayList<>();

=======
>>>>>>> Stashed changes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User user;

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 관계 설정용
    public void addImage(Image image) {
        images.add(image);
        image.connectNotice(this);
    }
}
