package gram11.doffice.global.error.handler;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;
import gram11.doffice.global.error.exception.ErrorResponse;
import gram11.doffice.global.webhook.DiscordWebhookClient;
import gram11.doffice.global.webhook.DiscordWebhookRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.NestedExceptionUtils.buildMessage;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final DiscordWebhookClient discordWebhookClient;

    // @Valid 검증 실패 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // DofficeException 처리
    @ExceptionHandler(DofficeException.class)
    public ResponseEntity<ErrorResponse> handleDofficeException(DofficeException e) {
        log.error("DofficeException : {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    // Security 관련 인증 실패 처리
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
        log.warn("Login Failed: {}", e.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;
        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // 예상치 못한 오류 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected Exception : {}", e.getMessage());

        sendDiscordAlert(e, request);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    private void sendDiscordAlert(Exception e, HttpServletRequest request) {
        try {
            String message = buildMessage(e, request);
            discordWebhookClient.send(
                    new DiscordWebhookRequest(message)
            );
        } catch (Exception ex) {
            log.error("Failed to send discord webhook", ex);
        }
    }

    private String buildMessage(Exception e, HttpServletRequest request) {
        return "Internal Server Error - Error: %s - URI: %s - Method: %s".formatted(e.toString(), request.getRequestURI(), request.getMethod());
    }
}
