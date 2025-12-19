package gram11.doffice.domain.post.presentaton.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostListResponse (
        Long id,
        String title,
        LocalDateTime createAt,
        String type
) { }
