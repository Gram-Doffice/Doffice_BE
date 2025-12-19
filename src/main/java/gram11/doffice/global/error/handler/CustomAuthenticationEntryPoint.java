package gram11.doffice.global.error.handler;

import gram11.doffice.global.error.exception.ResponseWithErrorCode;
import gram11.doffice.global.error.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseWithErrorCode responseWithErrorCode;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.warn("Unauthorized Access Attempt: {}", authException.getMessage());
        log.warn("Request URI: {}", request.getRequestURI());

        responseWithErrorCode.response(response, ErrorCode.UNAUTHORIZED_ACCESS);
    }
}