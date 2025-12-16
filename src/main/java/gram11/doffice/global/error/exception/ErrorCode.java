package gram11.doffice.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TITLE_IS_EMPTY(HttpStatus.BAD_REQUEST, "제목을 입력해주세요"),
    INVALID_TITLE_CHARS(HttpStatus.BAD_REQUEST, "제목에 특수문자가 포함되어 있습니다."),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다"),
    BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "잘못된 파일 확장자입니다"),
    MAX_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "최대 업로드 파일 크기를 초과했습니다"),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),

    NO_ADMIN(HttpStatus.FORBIDDEN, "관리자만 게시글을 관리할 수 있습니다"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류"),
    FAIL_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다");

    private final HttpStatus status;
    private final String message;
}
