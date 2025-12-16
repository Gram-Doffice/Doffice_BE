package gram11.doffice.domain.post.presentaton.dto.response;

import java.time.LocalDateTime;

public record PostListResponse (

        String title,
        LocalDateTime createAt,
        String type
) { }
