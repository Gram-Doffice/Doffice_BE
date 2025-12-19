package gram11.doffice.global.jwt.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class InvalidJwtException extends DofficeException {
    public static final InvalidJwtException EXCEPTION = new InvalidJwtException();
    private InvalidJwtException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
