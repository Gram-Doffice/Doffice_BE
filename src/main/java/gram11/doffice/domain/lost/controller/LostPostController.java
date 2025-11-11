package gram11.doffice.domain.lost.controller;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.dto.requestDto.LostPostRequestDto;
import gram11.doffice.domain.lost.dto.responseDto.LostPostResponseDto;
import gram11.doffice.domain.lost.service.LostPostService;
import gram11.doffice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lostposts")
@RequiredArgsConstructor
public class LostPostController {

    private final LostPostService lostPostService;

    // 전체 조회
    @GetMapping
    public List<LostPostResponseDto> getAll() {
        return lostPostService.getAllLostPosts().stream()
                .map(LostPostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @GetMapping("/{id}")
    public LostPostResponseDto get(@PathVariable Long id) {
        LostPost post = lostPostService.getLostPostById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));
        return LostPostResponseDto.fromEntity(post);
    }

    // 작성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LostPostResponseDto create(
            @RequestPart LostPostRequestDto dto,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal User user
    ) throws Exception {
        // 파일 처리를 Service에서 수행하도록 위임
        LostPost post = lostPostService.createLostPost(dto, user, files);
        return LostPostResponseDto.fromEntity(post);
    }

    // 수정 (NoticeController와 유사하게 @RequestBody 사용)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody LostPostRequestDto dto) {
        lostPostService.updateLostPost(id, dto);
        // NoticeController처럼 응답 본문 없이 204 반환
    }

    // 삭제 (NoticeController와 유사)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        lostPostService.deleteLostPost(id);
    }

    // 이미지 추가 (Notice에 없는 기능이므로, 파일 업로드 방식 유지)
    @PostMapping("/{id}/images")
    @ResponseStatus(HttpStatus.CREATED) // 이미지 추가 성공 시 201 반환
    public LostPostResponseDto addImages(
            @PathVariable Long id,
            @RequestPart List<MultipartFile> files // 파일 리스트를 받음
    ) throws Exception {
        LostPost post = lostPostService.addImages(id, files);
        return LostPostResponseDto.fromEntity(post);
    }

    // 이미지 삭제
    @DeleteMapping("/{id}/image/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeImage(@PathVariable Long id, @PathVariable Long imageId) {
        lostPostService.removeImage(id, imageId);
    }
}