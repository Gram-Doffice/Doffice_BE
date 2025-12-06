package gram11.doffice.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "Password Incorrect"),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post Not Found"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Image Not Found"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized Access"),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "No Permission"),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation Failed"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "Duplicate Reservation");

    private final HttpStatus status;
    private final String message;
}
