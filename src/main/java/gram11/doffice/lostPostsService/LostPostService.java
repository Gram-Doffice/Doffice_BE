package gram11.doffice.lostPostsService;

import gram11.doffice.entity.LostPost;
import gram11.doffice.repository.LostPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LostPostService {

    @Autowired
    private LostPostRepository repository;

    public Optional<LostPost> getLostPostById(Long id) {
        return repository.findById(id);
    }

    public List<LostPost> getAllLostPosts() {
        return repository.findAll();
    }

    public LostPost createLostPost(LostPost lostPost) {
        // id가 오면 무시되며 저장
        return repository.save(lostPost);
    }

    @Transactional
    public Optional<LostPost> updateLostPost(Long id, LostPost lostPost) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(lostPost.getTitle());
            existing.setContent(lostPost.getContent());
            return existing;
        });
    }

    public boolean deleteLostPost(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
