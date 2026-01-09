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
import gram11.doffice.global.config.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final NoticeService noticeService;
    private final LostService lostService;

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<Page<PostListResponse>> getAllNotice(
            @RequestParam("page") int page
    ) {
        return ResponseEntity.ok(postService.getAllPost(page));
    }

    // 공지사항 목록 조회
    @GetMapping("/notice")
    public ResponseEntity<Page<PostListResponse>> filterNotice(
            @RequestParam("page") int page
    ) {
        return ResponseEntity.ok(noticeService.filterNotice(page));
    }

    // 분실물 목록 조회
    @GetMapping("/lost")
    public ResponseEntity<Page<PostListResponse>> filterLost(
            @RequestParam("page") int page
    ) {
        return ResponseEntity.ok(lostService.filterLost(page));
    }

    // 공지글 작성
    @PostMapping("/write-notice")
    public ResponseEntity<Void> createNotice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateNoticeRequest request) {
        noticeService.createNotice(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 분실물 작성
    @PostMapping("/write-lost")
    public ResponseEntity<Void> createLost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestPart("request") CreateLostRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        lostService.createLost(request, images, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 공지글 수정
    @PutMapping("/modify-notice/{post_id}")
    public ResponseEntity<Void> updateNotice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post_id") Long id,
            @Valid @RequestBody UpdateNoticeRequest request) {
        noticeService.updateNotice(id, request, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    // 분실물 수정
    @PutMapping("/modify-lost/{post_id}")
    public ResponseEntity<Void> updateLost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post_id") Long id,
            @Valid @RequestPart("request") UpdateLostRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        lostService.updateLost(id, request, images, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    // 게시글 상세 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<PostDetailResponse> getNotice(@PathVariable("post_id") Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 게시글 삭제
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deleteNotice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post_id") Long id) {
        postService.deletePost(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}