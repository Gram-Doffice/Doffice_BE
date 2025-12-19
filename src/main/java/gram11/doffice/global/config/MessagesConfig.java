package gram11.doffice.global.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessagesConfig {

    // 1. MessageSource 빈을 등록하고 파일 경로를 명시합니다.
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // messages.properties 파일을 바라보게 설정
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

    // 2. LocalValidatorFactoryBean을 등록하여, Spring Validation이
    //    MessageSource를 사용하도록 강제 연결합니다.
    //    이 단계가 {키 값 그대로 출력} 문제를 해결하는 핵심입니다.
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}