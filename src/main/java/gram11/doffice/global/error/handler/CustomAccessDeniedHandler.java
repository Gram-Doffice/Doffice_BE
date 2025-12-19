package gram11.doffice.global.error.handler;

import gram11.doffice.global.error.exception.ResponseWithErrorCode;
import gram11.doffice.global.error.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseWithErrorCode responseWithErrorCode;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        log.error("Access Denied: {}", accessDeniedException.getMessage(), accessDeniedException);

        responseWithErrorCode.response(response, ErrorCode.FORBIDDEN_ACCESS);
    }
}