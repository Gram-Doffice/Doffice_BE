package gram11.doffice.domain.notice.entity;

import gram11.doffice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String title;

    @Column(columnDefinition = "varchar(2000)")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User user;

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
