package gram11.doffice.domain.lost.controller;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.dto.requestDto.AddImageDto;
import gram11.doffice.domain.lost.dto.requestDto.LostPostRequestDto;
import gram11.doffice.domain.lost.dto.responseDto.LostPostResponseDto;
import gram11.doffice.domain.lost.service.LostPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lostposts")
@RequiredArgsConstructor
public class LostPostController {

    private final gram11.doffice.domain.lost.service.LostPostService lostPostService;

    @GetMapping
    public List<LostPostResponseDto> getAll() {
        return lostPostService.getAllLostPosts().stream()
                .map(LostPostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LostPostResponseDto get(@PathVariable Long id) {
        LostPost post = lostPostService.getLostPostById(id).orElseThrow();
        return LostPostResponseDto.fromEntity(post);
    }

    @PostMapping
    public LostPostResponseDto create(@RequestBody LostPostRequestDto dto) {
        LostPost post = new LostPost();
        post.update(dto.getTitle(), dto.getContent());
        return LostPostResponseDto.fromEntity(lostPostService.createLostPost(post));
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
