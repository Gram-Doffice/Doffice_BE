package gram11.doffice.domain.lost.controller;


import gram11.doffice.domain.image.service.LostImageService;
import gram11.doffice.domain.lost.dto.requestDto.RequestLostDto;
import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.lost.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lost")
public class LostController {
    @Autowired
    LostService lostService;
    private final LostImageService lostImageService;

    //분실물 작성
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void creataLost(@RequestBody RequestLostDto requestLostDto) {
        lostService.createLost(requestLostDto);
    }

    //분실물 삭제
    @DeleteMapping("/{lost_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable("lost_id") Long parameter) {
        lostService.deleteLost(parameter);
    }

    //분실물 상세 조회
    @GetMapping("/{lost_id}")
    public Lost getLost(@PathVariable("lost_id") Long parameter) {
        return lostService.getLost(parameter);
    }

    // 전체 분실물 조회
    @GetMapping
    public List<Lost> getAllLost() {
        return lostService.getAllLost();
    }

    // 분실물 수정
    @PutMapping("/{lost_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLost(@PathVariable("lost_id") Long parameter, @RequestBody RequestLostDto requestLostDto) {
        lostService.updateLost(parameter, requestLostDto);
    }

    @PostMapping (value = "/{lost_id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) throws Exception {

        Lost lost = lostService.getLost(id);

        lostImageService.saveImages(files, lost);

        return ResponseEntity.ok(" ");
    }
}
