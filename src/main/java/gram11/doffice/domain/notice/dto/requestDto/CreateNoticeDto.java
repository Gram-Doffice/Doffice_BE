package gram11.doffice.domain.notice.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateNoticeDto {

    @NotBlank(message = "제목을 작성해주세요.")
    @Size(max = 100, message = "최대 100자까지 작성할 수 있습니다.")
    private String title;

    @Size(max = 2000, message = "최대 2000자까지 작성할 수 있습니다.")
    private String content;

    private List<MultipartFile> images;
}
