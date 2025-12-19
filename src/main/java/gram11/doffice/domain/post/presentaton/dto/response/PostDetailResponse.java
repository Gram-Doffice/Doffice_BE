package gram11.doffice.domain.post.presentaton.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse (

        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createAt,
        String type,
        List<String> imageUrl
) {}
