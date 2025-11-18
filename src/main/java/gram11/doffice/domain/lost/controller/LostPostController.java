package gram11.doffice.domain.lost.controller;

import gram11.doffice.domain.lost.entity.LostPost;
import gram11.doffice.domain.lost.lostPostsService.LostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lostposts")
public class LostPostController {

    @Autowired
    private LostPostService lostPostService;

    @GetMapping
    public ResponseEntity<List<LostPost>> getLostPosts() {
        return ResponseEntity.ok(lostPostService.getAllLostPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostPost> getLostPost(@PathVariable Long id) {
        return lostPostService.getLostPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LostPost> createLostPost(@RequestBody LostPost lostPost) {
        LostPost created = lostPostService.createLostPost(lostPost);
        URI location = URI.create("/api/lostposts/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LostPost> updateLostPost(@PathVariable Long id, @RequestBody LostPost lostPost) {
        return lostPostService.updateLostPost(id, lostPost)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostPost(@PathVariable Long id) {
        boolean deleted = lostPostService.deleteLostPost(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
