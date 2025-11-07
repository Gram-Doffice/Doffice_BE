package gram11.doffice.domain.image.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageUploadRequestDTO {

    private MultipartFile file;
}
