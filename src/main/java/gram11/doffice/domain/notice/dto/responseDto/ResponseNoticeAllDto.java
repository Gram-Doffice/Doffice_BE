package gram11.doffice.domain.notice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ResponseNoticeAllDto {

    private final List<NoticeDto> notice;

    @Getter
    @Builder
    public static class NoticeDto {
        private Long id;
        private String title;
        private LocalDateTime createdAt;
    }
}