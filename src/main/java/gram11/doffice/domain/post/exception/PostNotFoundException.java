package gram11.doffice.domain.post.exception;

import gram11.doffice.global.error.exception.DofficeException;
import gram11.doffice.global.error.exception.ErrorCode;

public class PostNotFoundException extends DofficeException {

    public static final DofficeException EXCEPTION = new PostNotFoundException();

    private PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
