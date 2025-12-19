package gram11.doffice.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorResponse;
import gram11.doffice.global.error.exception.ErrorCode;
import gram11.doffice.global.jwt.exception.ExpiredJwtException;
import gram11.doffice.global.jwt.exception.InvalidJwtException;
import io.sentry.Sentry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException catch : {}", e.getMessage());
            responseWithErrorCode(response, ErrorCode.EXPIRED_TOKEN);
            Sentry.captureException(e);
        } catch (InvalidJwtException e) {
            log.error("InvalidJwtException catch : {}", e.getMessage());
            responseWithErrorCode(response, ErrorCode.INVALID_TOKEN);
            Sentry.captureException(e);
        } catch (DofficeException e) {
            log.error("Handled DofficeException : ", e);
            responseWithErrorCode(response, e.getErrorCode());
            Sentry.captureException(e);
        } catch (Exception e) {
            log.error("Unhandled Exception : ", e);
            responseWithErrorCode(response, ErrorCode.INTERNAL_SERVER_ERROR);
            Sentry.captureException(e);
        }
    }

    private void responseWithErrorCode(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
