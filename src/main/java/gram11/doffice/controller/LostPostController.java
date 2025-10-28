package gram11.doffice.controller;

import gram11.doffice.entity.LostPost;
import gram11.doffice.lostPostsService.LostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lost")
public class LostPostController {

    @Autowired
    private LostPostService lostPostService;

    @GetMapping
    public ResponseEntity<List<LostPost>> getLostPosts() {
        return ResponseEntity.ok(lostPostService.getAllLostPosts());
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<LostPost> getLostPost(@PathVariable Long post_id) {
        return lostPostService.getLostPostById(post_id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LostPost> createLostPost(@RequestBody LostPost lostPost) {
        LostPost created = lostPostService.createLostPost(lostPost);
        URI location = URI.create("/lost" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<LostPost> updateLostPost(@PathVariable Long post_id, @RequestBody LostPost lostPost) {
        return lostPostService.updateLostPost(post_id, lostPost)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deleteLostPost(@PathVariable Long post_id) {
        boolean deleted = lostPostService.deleteLostPost(post_id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
