package gram11.doffice.domain.lost.service;

import gram11.doffice.domain.lost.dto.requestDto.RequestLostDto;
import gram11.doffice.domain.lost.entity.Lost;
import gram11.doffice.domain.lost.repository.LostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service

public class LostService {
    private LostRepository lostRepository;

    //분실물 작성
    public void createLost(RequestLostDto requestLostDto) {
        Lost lost = new Lost();
        lost.updateLost(requestLostDto.getTitle(), requestLostDto.getContent(), requestLostDto.getCreateDate());
        lostRepository.save(lost);
    }

    //분실물 조회
    public Lost getLost(Long id) {
        return lostRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
    }

    //분실물 수정
    @Transactional
    public void updateLost(Long Id, RequestLostDto requestLostDto) {
        Lost lost = lostRepository.findById(Id).orElseThrow(() -> new RuntimeException("공지 없음"));
        lost.updateLost(requestLostDto.getTitle(), requestLostDto.getContent(), requestLostDto.getCreateDate());
        lostRepository.save(lost);
    }

    //분실물 삭제
    public void deleteLost(Long Id) {
        lostRepository.deleteById(Id);
    }

    //분실물 전체 조회
    public List<Lost> getAllLost() {
        return lostRepository.findAll();
    }
}
