package gram11.doffice.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/sign-in/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/lost/**").permitAll()
                        .requestMatchers("/lost/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/notice/**").permitAll()
                        .requestMatchers("/notice/**").authenticated()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/auth/sign-in")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successForwardUrl("/auth/login/success")
                        .failureForwardUrl("/auth/login/failure")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/sign-out")
                        .logoutSuccessUrl("/auth/sign-out/success")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                )

                .httpBasic(httpBasic -> {});

        return http.build();
    }
}