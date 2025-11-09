package gram11.doffice.domain.lost.dto.responseDto;

import gram11.doffice.domain.lost.entity.LostPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class LostPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<String> images;

    public static LostPostResponseDto fromEntity(LostPost lostPost) {
        LostPostResponseDto dto = new LostPostResponseDto();
        dto.setId(lostPost.getId());
        dto.setTitle(lostPost.getTitle());
        dto.setContent(lostPost.getContent());
        dto.setImages(
                lostPost.getImages().stream()
                        .map(i -> i.getImageUrl())
                        .filter(url -> url != null)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
