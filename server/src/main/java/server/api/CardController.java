
package server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import commons.Card;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.async.DeferredResult;
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
     * This method receives and distributes cards between clients
     * @param c the card that the server has received and will send to all the clients on the network
     * @return a card
     */
    @MessageMapping("/cards") // /app/cards
    @SendTo("/topic/update")
    public Card addCard(Card c){
        add(c);
        return c;
    }

    /**
     * This method receives and distributes cards between clients
     * @param id the card's id that the server has received and will send to all the clients on the network
     * @return a card
     */
    @MessageMapping("/cardsDelete") // /app/cards
    @SendTo("/topic/update")
    public Long deleteCard(Long id){
        delete(id);
        return id;
    }

    /**
     * This method receives and distributes collections between clients
     * @param s a string that is needed for the method to work
     * @return a collection
     */
    @MessageMapping("/cardsDeleteAll") // /app/collectionsDelete
    @SendTo("/topic/update")
    public Card deleteAllCards(Card s){
        deleteAll();
        return s;
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
     * the put API for the card object
     * @param id the id of the card
     * @param updatedCard the new data it should have
     * @return the responseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable("id") long id, @RequestBody Card updatedCard) {

        //check if card is in database
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Card cardInDatabase = repo.findById(id).get();

        // update the property's
        cardInDatabase.setDescription(updatedCard.getDescription());
        cardInDatabase.setTitle(updatedCard.getTitle());
        cardInDatabase.setCollectionId(updatedCard.getCollectionId());
        cardInDatabase.setSubtasks(updatedCard.getSubtasks());
        cardInDatabase.setColorPreset(updatedCard.getColorPreset());

        // save the card
        Card theSavedCard = repo.save(cardInDatabase);
        return ResponseEntity.ok(theSavedCard);
    }


    /**
     * get the cards by the id of collectioni
     * @param id the id of the collection
     * @return the cards
     */
    @GetMapping("/{id}/ofCollection")
    public ResponseEntity<List<Card>> getCardsByIdOfCollection(@PathVariable long id) {
        List<Card> allCards = repo.findAll(Sort.by(Sort.Direction.ASC, "index"));
        List<Card> res = new ArrayList<>();

        for (Card c : allCards) {
            if (c.getCollectionId() != null && c.getCollectionId() == id) {
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
        listeners.values().forEach(l -> {
            l.accept(id);
        });

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<Object, Consumer<Long>> listeners = new ConcurrentHashMap<>();


    /**
     * This gets updates from the delete by Id method and saves this within a listener.
     * @return the reponse (long if change occured/no content if no change)
     */
    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Long>> getUpdates(){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Long>>(5000L, noContent);

        var key = new Object();
        listeners.put(key, l -> {
            result.setResult(ResponseEntity.ok(l));
        });

        result.onCompletion(() -> listeners.remove(key));

        return result;
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