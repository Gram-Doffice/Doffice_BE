package gram11.doffice.domain.post.domain;

import gram11.doffice.domain.post.domain.type.PostType;
import gram11.doffice.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(length = 1024)
    private List<String> imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Post(String title, String content, PostType postType, List<String> imageUrl, User user) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.createdAt = LocalDateTime.now();
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public void updatePost(String title, String content, List<String> imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
