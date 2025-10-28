package gram11.doffice.domain.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Manager {

    @Id
    @Column(name = "id", length = 20, nullable = false)
    private String id = "ThanksToApple";

    @Column(name = "password", length = 20, nullable = false)
    private String password = "qwer1234";
}
