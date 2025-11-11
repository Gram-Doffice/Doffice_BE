package gram11.doffice.domain.lost.dto.responseDto;

import gram11.doffice.domain.lost.entity.LostPost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 파일명: LostPostResponseDto.java
@Getter
@Setter
public class LostPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<String> images; // 이미지 URL 리스트 포함
    private LocalDateTime createdAt;

    // 엔티티를 DTO로 변환하는 정적 팩토리 메서드 유지
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
        dto.setCreatedAt(lostPost.getCreatedAt());
        return dto;
    }
}

