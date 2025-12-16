package gram11.doffice.domain.user.sevice;

import gram11.doffice.domain.user.domain.type.Role;
import gram11.doffice.domain.user.domain.repository.UserRepository;
import gram11.doffice.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${manager.initial.username}")
    private String initialUsername;

    @Value("${manager.initial.password}")
    private String initialPassword;

    @Value("${manager.initial.enabled}") // 초기화 기능 활성화 여부
    private boolean enabled;

    @Override
    public void run(String... args) throws Exception {
        if (!enabled) return;

        if (userRepository.findByUsername(initialUsername).isEmpty()) {
            User manager = new User();
            manager.updateUser(
                    initialUsername,
                    encoder.encode(initialPassword),
                    Role.MANAGER
            );
            userRepository.save(manager);
        }
    }
}
