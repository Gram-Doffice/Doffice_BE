package gram11.doffice.domain.lost.dto.responseDto;

import gram11.doffice.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResponseLostDto {

    private String title;
    private String content;
    private User user;
}
