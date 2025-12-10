package gram11.doffice.domain.post.presentaton.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateLostRequest(
        String title,
        String content,
        List<MultipartFile> images
) {
}
