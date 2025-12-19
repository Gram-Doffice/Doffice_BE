package gram11.doffice.domain.post.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class ImageExceededException extends DofficeException {
    public static final DofficeException EXCEPTION = new ImageExceededException();
    private ImageExceededException() {
        super(ErrorCode.IMAGE_EXCEEDED);
    }
}