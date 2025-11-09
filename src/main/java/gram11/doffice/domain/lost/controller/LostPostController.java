package gram11.doffice.domain.lost.controller;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.dto.requestDto.AddImageDto;
import gram11.doffice.domain.lost.dto.requestDto.LostPostRequestDto;
import gram11.doffice.domain.lost.dto.responseDto.LostPostResponseDto;
import gram11.doffice.domain.lost.service.LostPostService;
import gram11.doffice.domain.user.entity.User; // ⭐ User 엔티티 import
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // ⭐ 인증 정보 주입용
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException; // ⭐ 예외 처리용

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lostposts")
@RequiredArgsConstructor
public class LostPostController {

    private final LostPostService lostPostService;

    @GetMapping
    public List<LostPostResponseDto> getAll() {
        return lostPostService.getAllLostPosts().stream()
                .map(LostPostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LostPostResponseDto get(@PathVariable Long id) {
        LostPost post = lostPostService.getLostPostById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시글을 찾을 수 없습니다."));
        return LostPostResponseDto.fromEntity(post);
    }

    // ⭐ 수정: User 인증 정보를 받아 Service에 전달
    @PostMapping
    public LostPostResponseDto create(
            @RequestBody LostPostRequestDto dto,
            @AuthenticationPrincipal User user // ⭐ 로그인된 User 정보 (작성자)
    ) {
        // User 객체를 서비스로 전달하여 LostPost 생성자에 사용
        LostPost post = lostPostService.createLostPost(dto, user);
        return LostPostResponseDto.fromEntity(post);
    }

    @PutMapping("/{id}")
    public LostPostResponseDto update(@PathVariable Long id, @RequestBody LostPostRequestDto dto) {
        LostPost post = lostPostService.updateLostPost(id, dto.getTitle(), dto.getContent());
        return LostPostResponseDto.fromEntity(post);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        lostPostService.deleteLostPost(id);
    }

    @PostMapping("/{id}/image-url")
    public LostPostResponseDto addImage(@PathVariable Long id, @RequestBody AddImageDto dto) {
        LostPost post = lostPostService.addImage(id, dto);
        return LostPostResponseDto.fromEntity(post);
    }

    @DeleteMapping("/{id}/image/{imageId}")
    public LostPostResponseDto removeImage(@PathVariable Long id, @PathVariable Long imageId) {
        LostPost post = lostPostService.removeImage(id, imageId);
        return LostPostResponseDto.fromEntity(post);
    }
}