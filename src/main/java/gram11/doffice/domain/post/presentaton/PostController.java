package gram11.doffice.domain.post.presentaton;

import gram11.doffice.domain.post.presentaton.dto.request.CreateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.request.CreateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateLostRequest;
import gram11.doffice.domain.post.presentaton.dto.request.UpdateNoticeRequest;
import gram11.doffice.domain.post.presentaton.dto.response.PostDetailResponse;
import gram11.doffice.domain.post.presentaton.dto.response.PostListResponse;
import gram11.doffice.domain.post.service.LostService;
import gram11.doffice.domain.post.service.NoticeService;
import gram11.doffice.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final NoticeService noticeService;
    private final LostService lostService;

    // 게시글 상세 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<PostDetailResponse> getNotice(@PathVariable("post_id") Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllNotice() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    // 공지사항 목록 조회
    @GetMapping("/notice")
    public ResponseEntity<List<PostListResponse>> filterNotice() {
        return ResponseEntity.ok(noticeService.filterNotice());
    }

    // 분실물 목록 조회
    @GetMapping("/lost")
    public ResponseEntity<List<PostListResponse>> filterLost() {
        return ResponseEntity.ok(lostService.filterLost());
    }

    // 게시글 삭제
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("post_id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 공지글 작성
    @PostMapping("/write-notice")
    public ResponseEntity<Void> createNotice(
            @Valid @ModelAttribute("request") CreateNoticeRequest request) {
        noticeService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 공지글 수정
    @PutMapping("/modify-notice/{post_id}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable("post_id") Long id,
            @Valid @ModelAttribute("request") UpdateNoticeRequest request) {
        noticeService.updateNotice(id, request);
        return ResponseEntity.noContent().build();
    }

    // 분실물 작성
    @PostMapping("/write-lost")
    public ResponseEntity<Void> createLost(
            @Valid @ModelAttribute("request") CreateLostRequest request) {
        lostService.createLost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 분실물 수정
    @PutMapping("/modify-lost/{post_id}")
    public ResponseEntity<Void> updateLost(
            @PathVariable("post_id") Long id,
            @Valid @ModelAttribute("request") UpdateLostRequest request) {
        lostService.updateLost(id, request);
        return ResponseEntity.noContent().build();
    }
}
