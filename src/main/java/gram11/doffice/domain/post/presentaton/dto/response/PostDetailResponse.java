package gram11.doffice.domain.post.presentaton.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostDetailResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse user;
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
