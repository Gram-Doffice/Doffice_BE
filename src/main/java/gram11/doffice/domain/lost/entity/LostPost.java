package gram11.doffice.domain.lost.entity;

import gram11.doffice.domain.image.entity.Image;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "lostPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    // 글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 이미지 추가
    public void addImage(Image image) {
        images.add(image);
    }

    // 이미지 삭제
    public void removeImage(Image image) {
        images.remove(image);
    }
}
