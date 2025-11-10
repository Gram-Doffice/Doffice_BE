package gram11.doffice.domain.notice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseNoticeAllDto {

    private List<NoticeDto> notice;

    @Getter
    @Builder
    public static class NoticeDto {
        private Long id;
        private String title;
    }
}
