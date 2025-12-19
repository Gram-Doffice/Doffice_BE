package gram11.doffice.global.jwt.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class ExpiredJwtException extends DofficeException {
    public static final ExpiredJwtException EXCEPTION = new ExpiredJwtException();
    private ExpiredJwtException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
