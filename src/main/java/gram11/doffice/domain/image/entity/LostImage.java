package gram11.doffice.domain.image.entity;

import gram11.doffice.domain.lost.entity.Lost;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class LostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id")
    private Lost lost;

    public void connectLost(Lost lost) {
        this.lost = lost;
    }

    public static LostImage createLost(String imageUrl, Lost lost) {
        LostImage lostImage = new LostImage();
        lostImage.imageUrl = imageUrl;
        lostImage.lost = lost;
        return lostImage;
    }

}