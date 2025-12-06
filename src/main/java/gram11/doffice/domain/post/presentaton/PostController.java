package gram11.doffice.domain.post.presentaton;

import gram11.doffice.domain.post.presentaton.dto.request.CreatePostRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdatePostRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostResponse;
import gram11.doffice.domain.post.presentaton.dto.response.PostDetailResponse;
import gram11.doffice.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/notice")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 공지글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createNotice(
            @Valid @ModelAttribute("request") CreatePostRequest noticeDto)
            throws IOException {
        postService.createNotice(noticeDto);
    }

    // 공지글 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{notice_id}")
    public void deleteNotice(@PathVariable("notice_id") Long parameter) {
        postService.deleteNotice(parameter);
    }

    // 공지글 상세 조회
    @GetMapping("/{notice_id}")
    public PostDetailResponse getNotice(@PathVariable("notice_id") Long parameter) {
        return postService.getNotice(parameter);
    }

    // 전체 공지사항 조회
    @GetMapping
    public PostResponse getAllNotice() {
        return postService.getAllNotice();
    }

    // 공지글 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{notice_id}")
    public void updateNotice(
            @PathVariable("notice_id") Long parameter,
            @Valid @ModelAttribute("request") UpdatePostRequest noticeDto)
            throws IOException {

        postService.updateNotice(parameter, noticeDto);
    }
}
