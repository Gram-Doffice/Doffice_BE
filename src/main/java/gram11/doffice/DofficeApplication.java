package gram11.doffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DofficeApplication {
	public static void main(String[] args) {
		SpringApplication.run(DofficeApplication.class, args);
	}
}
