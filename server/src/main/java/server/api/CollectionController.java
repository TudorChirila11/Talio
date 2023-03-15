
package server.api;

import java.util.List;

import commons.Board;
import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Collection;
import server.database.CollectionRepository;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionRepository repo;

    /**
     * Controller constructor
     * @param repo repo reference
     */
    public CollectionController(CollectionRepository repo) {
        this.repo = repo;

    }

    /**
     * Hardcoded mapping all collections
     * @return List of collections objectss
     */
    @GetMapping(path = { "", "/" })
    public List<Collection> getAll() {
        return repo.findAll();
    }

    /**
     * getById method for a Collection with path var
     * @param id of Collection
     * @return a response Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Initalization of a collection
     * @param collection a new collection obj
         * @return the Response Entity
     */
    @PostMapping(path = { "/addCollection" })
    public ResponseEntity<Collection> add(@RequestBody Collection collection) {

        Collection saved = repo.save(collection);
        return ResponseEntity.ok(saved);
    }

    /**
     * adding a card to a collection
     * @param id of the collection
     * @param card the card to be added
     * @return the Response Entity
     */
    @PostMapping(path = { "/addCardTo/{id}" })
    public ResponseEntity<Collection> add(@PathVariable("id") long id, @RequestBody Card card) {
        System.out.println(card);
        System.out.println("id is equal to = " + id);

        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Collection c = repo.findById(id).orElse(null);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }

        c.addCard(card);
        Collection updatedCollection = repo.save(c);
        return ResponseEntity.ok(updatedCollection);
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

}