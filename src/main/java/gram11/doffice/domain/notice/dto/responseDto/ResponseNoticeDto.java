package gram11.doffice.domain.notice.dto.responseDto;

import gram11.doffice.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseNoticeDto {

    private String title;
    private String content;
    private User user;
}
