package gram11.doffice.global.error;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;
import gram11.doffice.global.error.exception.ResponseWithErrorCode;
import gram11.doffice.global.jwt.exception.ExpiredJwtException;
import gram11.doffice.global.jwt.exception.InvalidJwtException;
import io.sentry.Sentry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ResponseWithErrorCode responseWithErrorCode;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException catch : {}", e.getMessage());
            responseWithErrorCode.response(response, ErrorCode.EXPIRED_TOKEN);
        } catch (InvalidJwtException e) {
            log.error("InvalidJwtException catch : {}", e.getMessage());
            responseWithErrorCode.response(response, ErrorCode.INVALID_TOKEN);
        } catch (DofficeException e) {
            log.error("Handled DofficeException : ", e);
            responseWithErrorCode.response(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("Unhandled Exception : ", e);
            responseWithErrorCode.response(response, ErrorCode.INTERNAL_SERVER_ERROR);
            Sentry.captureException(e);
        }
    }
}
