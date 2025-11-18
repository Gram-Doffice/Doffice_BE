package gram11.doffice.domain.notice.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ResponseNoticeDto {

    private Long id;
    private String title;
    private String content;
    private ResponseUserDto user;
    private LocalDateTime createdAt;

    private List<ImageDto> images;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ImageDto {
        private Long id;
        private String imageUrl;
    }
}
