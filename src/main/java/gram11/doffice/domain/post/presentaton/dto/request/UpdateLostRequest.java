package gram11.doffice.domain.post.presentaton.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateLostRequest(

        @NotBlank(message = "{validation.title.blank}")
        @Size(max = 200, message = "{validation.title.length}")
        String title,

        @NotBlank(message = "{validation.content.blank}")
        @Size(max = 2000, message = "{validation.content.length}")
        String content,

        // valid 뭐해야하지ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ extension 체크용 어노테이션 정의?
        List<String> keepImagesUrl
) {
}