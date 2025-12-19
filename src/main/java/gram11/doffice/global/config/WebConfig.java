package gram11.doffice.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://gram-doffice.ncloud.sbs")
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTION")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}