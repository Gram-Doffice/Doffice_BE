package gram11.doffice.global.s3.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class WrongFileUrlException extends DofficeException {
    public static final DofficeException EXCEPTION = new WrongFileUrlException();
    private WrongFileUrlException() {
        super(ErrorCode.WRONG_FILE_URL);
    }
}
