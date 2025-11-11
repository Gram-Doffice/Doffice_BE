package gram11.doffice.domain.lost.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor; // Notice DTO처럼 NoArgsConstructor 추가
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostPostRequestDto {
    private String title;
    private String content;

    // validation 설정은 필요 시 추가
}


