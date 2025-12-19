package gram11.doffice.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class DofficeException extends RuntimeException {

    private final ErrorCode errorCode;

    public DofficeException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public DofficeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
