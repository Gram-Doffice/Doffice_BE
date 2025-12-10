package gram11.doffice.global.s3.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class FailUploadImageException extends DofficeException {

    public FailUploadImageException(Throwable cause) {
        super(ErrorCode.FAIL_UPLOAD_IMAGE, cause);
    }
}
