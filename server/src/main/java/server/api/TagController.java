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
     * @param t the tag that the server has received and will send to all the clients on the network
     * @return a collection
     */
    @MessageMapping("/tagsDelete") // /app/collections
    @SendTo("/topic/update")
    public Tag deleteTag(Tag t){
        delete(t.getId());
        return t;
    }

    /**
     * This method receives and distributes tags between clients
     * @param t the tag that the server has received and will send to all the clients on the network
     * @return a collection
     */
    @MessageMapping("/tagsUpdate") // /app/collections
    @SendTo("/topic/update")
    public Tag editTag(Tag t){
        updateTag(t.getId(), t);
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
     * Hardcoded mapping all tags
     * @param boardId the id which will be used to get only the tags that the client needs
     * @return List of tags objects
     */
    @GetMapping(path = {"/{id}"})
    public List<Tag> getAllInBoard(@PathVariable("id") long boardId) {
        System.out.println(boardId);
        return repo.findByBoardId(boardId);
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

    /**
     * this will delete the collection
     * @param id the id / name of the collection
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    /**
     * the put API for the tag object
     * @param id the id of the tag to update
     * @param newTag the data the object should have
     * @return the responseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody Tag newTag) {

        // test if we have the collection in the database
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // find the collection in the database
        Tag tagInDatabase = repo.findById(id).get();

        // set the property's
        tagInDatabase.setName(newTag.getName());

        // remove the cards
        tagInDatabase.setColour(newTag.getColour());

        // store the collection
        Tag theSavedTag = repo.save(tagInDatabase);

        return ResponseEntity.ok(theSavedTag);
    }


}
