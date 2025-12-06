package gram11.doffice.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorResponse;
import gram11.doffice.global.error.exception.ErrorCode;
import io.sentry.Sentry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (DofficeException e) {
            log.error("Handled DofficeException : ", e);
            Sentry.captureException(e);
        } catch (Exception e) {
            log.error("Unhandled Exception : ", e);
            Sentry.captureException(e);
        }
    }

    private void responseWithErrorCode(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse body = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), body);
    }
}
