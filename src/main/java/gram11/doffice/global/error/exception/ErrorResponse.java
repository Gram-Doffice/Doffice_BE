package gram11.doffice.global.error.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
public record ErrorResponse(
        HttpStatus status,
        String message,
        Map<String, String> errors
) { }