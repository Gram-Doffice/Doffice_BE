package gram11.doffice.domain.post.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class NoAuthorException extends DofficeException {
    public static final DofficeException EXCEPTION = new NoAuthorException();
    private NoAuthorException() {
        super(ErrorCode.NO_AUTHOR);
    }
}
