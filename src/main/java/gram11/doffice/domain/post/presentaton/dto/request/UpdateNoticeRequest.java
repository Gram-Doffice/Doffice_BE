package gram11.doffice.domain.post.presentaton.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UpdateNoticeRequest {

    @NotBlank(message = "제목을 작성해주세요.")
    @Size(max = 100, message = "최대 100자까지 작성할 수 있습니다.")
    private String title;

    @Size(max = 2000, message = "최대 2000자까지 작성할 수 있습니다.")
    private String content;

    private List<MultipartFile> images;
}
