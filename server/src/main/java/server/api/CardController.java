
package server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * get by string title method for a Card
     * @param title of Card
     * @return a response Entity
     */
    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<Card>> getByTitle(@PathVariable("title") String title) {
        List<Card> resCards = new ArrayList<>();
        List<Card> allCards = repo.findAll();

        // test if cards have the right title add it to return
        for (Card card : allCards) {
            if (title.equals(card.getTitle())) {
                resCards.add(card);
            }
        }
        // check if no cards are found
        if (resCards.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resCards);
        }
    }


    /**
     * get the cards by the id of collectioni
     * @param id the id of the collection
     * @return the cards
     */
    @GetMapping("/{id}/ofCollection")
    public ResponseEntity<List<Card>> getCardsByIdOfCollection(@PathVariable long id) {
        List<Card> allCards = repo.findAll();
        List<Card> res = new ArrayList<>();

        for (Card c : allCards) {
            if (c.getCollectionId() == id) {
                res.add(c);
            }
        }

        return ResponseEntity.ok(res);


    }

    /**
     * this will delete the Card
     * @param id the id / name of the Card
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
     * deletes all the Cards in the database
     * @return responseEntity
     */
    @DeleteMapping(path = { "", "/" })
    public ResponseEntity<Void> deleteAll() {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }

    /**
     * Initalization of a card
     * @param card a new card obj
     * @return the Response Entity
     */
    @PostMapping(path = { "/", ""})
    public ResponseEntity<Card> add(@RequestBody Card card){
        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }



}