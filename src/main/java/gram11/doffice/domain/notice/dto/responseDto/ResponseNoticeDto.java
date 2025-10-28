package gram11.doffice.domain.notice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseNoticeDto {

    private Long id;
    private String title;
    private String content;
    private String manager;
}
