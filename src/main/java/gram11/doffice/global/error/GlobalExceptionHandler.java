package gram11.doffice.global.error;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> validatorExceptionHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        400,
                        "COMMON-400",
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> propertyReferenceExceptionHandler(PropertyReferenceException e) {
        return new ResponseEntity<>(new ErrorResponse(400, "COMMON-400", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("파일 처리 중 오류가 발생했습니다.");
    }
}
