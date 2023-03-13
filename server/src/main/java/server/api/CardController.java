
package server.api;

import java.util.List;

import commons.Card;
import commons.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Card;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardRepository repo;

    /**
     * Controller constructor
     * @param repo repo reference
     */
    public CardController(CardRepository repo) {
        this.repo = repo;

        // TODO remove this when testing is done
        this.repo.save(new Card("card 1 test"));
        this.repo.save(new Card("card 2 test"));
    }

    /**
     * Hardcoded mapping all cards
     * @return List of cards objects
     */
    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    /**
     * getById method for a Card with path var
     * @param id of Card
     * @return a response Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * this will delete the Card
     * @param id the id / name of the Card
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