package gram11.doffice.domain.notice.entity;

import gram11.doffice.domain.manager.entity.Manager;
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

    @ManyToOne()
    private Manager manager;

    @Builder
    public Notice(String title, String content, Manager manager) {
        this.title = title;
        this.content = content;
        this.manager = manager;
    }

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
