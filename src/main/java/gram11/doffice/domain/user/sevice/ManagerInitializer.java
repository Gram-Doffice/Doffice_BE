package gram11.doffice.domain.user.sevice;

import gram11.doffice.domain.user.entity.type.Role;
import gram11.doffice.domain.user.repository.UserRepository;
import gram11.doffice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerInitializer implements CommandLineRunner {

    // final로 주입하기 이유는 몰루...
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String username = "ThanksToApple";

        if (userRepository.findByUsername(username).isEmpty()) {
            User manager = new User();
            manager.updateUser(
                    username,
                    passwordEncoder.encode("qwer1234"),
                    Role.MANAGER
            );
            userRepository.save(manager);
        }
    }
}
