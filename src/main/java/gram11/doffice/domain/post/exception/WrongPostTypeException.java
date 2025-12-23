package gram11.doffice.domain.post.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class WrongPostTypeException extends DofficeException {
    public static final DofficeException EXCEPTION = new WrongPostTypeException();
    private WrongPostTypeException() {
        super(ErrorCode.WRONG_POST_TYPE);
    }
}