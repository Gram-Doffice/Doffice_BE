package gram11.doffice.domain.notice.dto.responseDto;

import gram11.doffice.domain.user.dto.ResponseUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseNoticeDto {

    private Long id;
    private String title;
    private String content;
    private ResponseUserDto user;
}
