package gram11.doffice.global.error.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // JSON 응답을 위한 ObjectMapper를 사용할 수도 있지만, 여기서는 간단하게 처리합니다.
    // private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        // 💡 1. 401 에러 발생 시 로그 출력
        // 토큰이 없거나, 만료되었거나, 서명이 유효하지 않을 때 발생
        log.warn("Unauthorized Access Attempt: {}", authException.getMessage());
        log.warn("Request URI: {}", request.getRequestURI());

        // 💡 2. 클라이언트에게 401 응답 전달
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        // JSON 응답 본문을 보내 클라이언트에게 상세 정보를 제공
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{"
                + "\"status\": 401,"
                + "\"error\": \"Unauthorized\","
                + "\"message\": \"인증에 실패하였습니다: 유효하지 않거나 만료된 토큰\""
                + "}");
    }
}