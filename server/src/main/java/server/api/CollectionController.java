
package server.api;

import java.util.List;

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

        // TODO remove this when testing is done
        this.repo.save(new Collection("test1"));
        this.repo.save(new Collection("test2"));
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
    public ResponseEntity<Collection> getById(@PathVariable("id") String id) {
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
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Collection> add(@RequestBody Collection collection) {

        Collection saved = repo.save(collection);
        return ResponseEntity.ok(saved);
    }

    /**
     * this will delete the collection
     * @param id the id / name of the collection
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}