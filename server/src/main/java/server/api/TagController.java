package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

import java.util.List;


@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository repo;

    /**
     * Controller constructor
     *
     * @param repo repo reference
     */
    public TagController(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * This method receives and distributes tags between clients
     * @param t the tag that the server has received and will send to all the clients on the network
     * @return a collection
     */
    @MessageMapping("/tags") // /app/collections
    @SendTo("/topic/update")
    public Tag addTag(Tag t){
        add(t);
        return t;
    }

    /**
     * This method receives and distributes tags between clients
     * @param t a tag that is needed for the method to work
     * @return a collection
     */
    @MessageMapping("/tagsDeleteAll") // /app/tagsDeleteAll
    @SendTo("/topic/update")
    public Tag deleteAllTags(Tag t){
        deleteAll();
        return t;
    }

    /**
     * Initialization of a board
     *
     * @param tag a new tag obj
     * @return the Response Entity
     */
    @PostMapping(path = {"/", ""})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * Hardcoded mapping all tags
     *
     * @return List of tags objects
     */
    @GetMapping(path = {"", "/"})
    public List<Tag> getAll() {
        return repo.findAll();
    }

    /**
     * delete all the tags in the database
     * @return responseEntity
     */
    @DeleteMapping(path = { "", "/" })
    public ResponseEntity<Void> deleteAll() {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }


}
