package gram11.doffice.global.s3.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class BadFileExtensionException extends DofficeException {

    public static final DofficeException EXCEPTION = new BadFileExtensionException();

    private BadFileExtensionException() {
        super(ErrorCode.BAD_FILE_EXTENSION);
    }
}
