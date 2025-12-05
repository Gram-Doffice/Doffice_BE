package gram11.doffice.domain.lost.controller;

import gram11.doffice.domain.lost.dto.requestDto.RequestLostDto;
import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.lost.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/lost")
public class LostController {

    private final LostService lostService;

    private final ObjectMapper objectMapper;

    // 분실물 작성 & 이미지 업로드
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createLost(
            @RequestPart("data") String requestLostDtoJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {


        RequestLostDto requestLostDto = objectMapper.readValue(requestLostDtoJson, RequestLostDto.class);

        lostService.createLost(requestLostDto, files);
    }

    // 분실물 수정
    @PutMapping(value = "/post/lost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLost(
            @PathVariable("/post/lost") Long lostId,
            @RequestPart("data") String requestLostDtoJson,
            @RequestPart(value = "newFiles", required = false) List<MultipartFile> newFiles,
            @RequestPart(value = "deletedImageIds", required = false) List<Long> deletedImageIds) throws IOException {

        // JSON 문자열을 DTO 객체로 변환
        RequestLostDto requestLostDto = objectMapper.readValue(requestLostDtoJson, RequestLostDto.class);

        lostService.updateLost(lostId, requestLostDto, newFiles, deletedImageIds);
    }

    // 분실물 삭제
    @DeleteMapping("/post/lost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLost(@PathVariable("/post/lost") Long lostId) {
        lostService.deleteLost(lostId);
    }

    @GetMapping("/post/lost")
    public Lost getLost(@PathVariable("/post/lost") Long lostId) {
        return lostService.getLost(lostId);
    }

    @GetMapping
    public List<Lost> getAllLost() {
        return lostService.getAllLost();
    }
}