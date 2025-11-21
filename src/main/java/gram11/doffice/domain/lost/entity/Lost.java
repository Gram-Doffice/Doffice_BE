package gram11.doffice.domain.lost.entity;


import gram11.doffice.domain.image.entity.LostImage;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EnableJpaAuditing

public class Lost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createdDate;

   // @ManyToOne(fetch = FetchType.LAZY)
   //@JoinColumn(name = "manager_id", nullable = false)
   // private User user;

    @OneToMany(mappedBy = "lost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LostImage> lostImages = new ArrayList<>();

    @Builder
    public Lost(String title, String content) {
        this.title = title;
        this.content = content;
        //this.user = user;
    }

    public void updateLost(String title, String content, LocalDateTime createDate) {
        this.title = title;
        this.content = content;
        this.createdDate = createDate;
    }
}