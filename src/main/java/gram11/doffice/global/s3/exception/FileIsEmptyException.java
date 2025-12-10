package gram11.doffice.global.s3.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class FileIsEmptyException extends DofficeException {

    public static final DofficeException EXCEPTION = new FileIsEmptyException();

    private FileIsEmptyException() {
        super(ErrorCode.FILE_IS_EMPTY);
    }
}
