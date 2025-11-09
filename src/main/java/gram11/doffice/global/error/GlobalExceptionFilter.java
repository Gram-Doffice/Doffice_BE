<<<<<<< Updated upstream
=======
package gram11.doffice.global.error;

import io.sentry.Sentry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class GlobalExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    private void writerErrorCode(HttpServletResponse response, Errorcode errorcode)
    throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                errorcode.getStatus(), errorcode.getCode(), errorcode.getMessage());

        response.setStatus(errorcode.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorResponse.toString());
    }
}
>>>>>>> Stashed changes
