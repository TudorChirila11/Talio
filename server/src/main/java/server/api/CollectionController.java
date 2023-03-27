
package server.api;

import java.util.List;
import java.util.Optional;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Collection;
import server.database.CardRepository;
import server.database.CollectionRepository;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionRepository repoCollection;
    private final CardRepository repoCard;

    /**
     * Controller constructor
     * @param repoCollection repo reference
     * @param repoCard the repository of the cards
     */
    public CollectionController(CollectionRepository repoCollection, CardRepository repoCard) {
        this.repoCollection = repoCollection;
        this.repoCard = repoCard;

    }

    /**
     * Hardcoded mapping all collections
     * @return List of collections objectss
     */
    @GetMapping(path = { "", "/" })
    public List<Collection> getAll() {
        return repoCollection.findAll();
    }

    /**
     * getById method for a Collection with path var
     * @param id of Collection
     * @return a response Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getById(@PathVariable("id") long id) {
        if (!repoCollection.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repoCollection.findById(id).get());
    }

    /**
     * get the cards of a collection
     * @param id of Collection
     * @return a response Entity
     */
    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCardsByCollectionId(@PathVariable long id) {
        // collection could not be found.
        Optional<Collection> collectionOpt = repoCollection.findById(id);

        // test if empty give bad response
        if (collectionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // return the cards
        List<Card> cards = collectionOpt.get().getCards();
        return ResponseEntity.ok(cards);
    }

//    api/collection/{collectionId}/deleteCard/{position}'

    /**
     * this API deletes the card at a specific position in a collection.
     * @param collectionId the id of the collection to change
     * @param index the index of the card to remove
     * @return Response Entity
     */
    @DeleteMapping("{collectionId}/deleteCard/{index}")
    public ResponseEntity<Collection> deleteCardAtPosition
    (@PathVariable long collectionId, @PathVariable int index) {

        Optional<Collection> collectionOpt = repoCollection.findById(collectionId);

        // the collection is not found
        if (collectionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Collection collection = collectionOpt.get();
        List<Card> cards = collection.getCards();

        // making sure that the position is filled
        assert cards.size() > 0;
        assert (index >= 0 && index < cards.size());

        cards.remove(index);

        // store new list
        Collection updatedCollection = repoCollection.save(collection);
        return ResponseEntity.ok(updatedCollection);

    }

    /**
     * Initalization of a collection
     * @param collection a new collection obj
         * @return the Response Entity
     */
    @PostMapping(path = { "/", "" })
    public ResponseEntity<Collection> add(@RequestBody Collection collection) {

        Collection saved = repoCollection.save(collection);
        return ResponseEntity.ok(saved);
    }

    /**
     * this will delete the collection
     * @param id the id / name of the collection
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (!repoCollection.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repoCollection.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete all the collections in the database
     * @return responseEntity
     */
    @DeleteMapping(path = { "", "/" })
    public ResponseEntity<Void> deleteAll() {
        repoCollection.deleteAll();
        return ResponseEntity.noContent().build();
    }

    /**
     * add a card to a collection methode
     * @param idCard the id of the card
     * @param idCollection the id of the collection
     * @return responseEntity
     */
    @PostMapping(path = {"/CardAddTo/{id_card}/{id_collection}"})
    public ResponseEntity<Collection> add(@PathVariable("id_card") long idCard,
                                          @PathVariable("id_collection") long idCollection) {

        // checking if both card and collection exist
        if (!repoCollection.existsById(idCollection) || !repoCard.existsById(idCard)) {
            return ResponseEntity.badRequest().build();
        }

        // getting the card and collection
        Collection collection = repoCollection.findById(idCollection).orElse(null);
        Card card = repoCard.findById(idCard).orElse(null);

        // not sure why but this check is important (if you know tell me -Teun)
        if (collection == null || card == null) {
            return ResponseEntity.notFound().build();
        }

        // adding the card to the collection
        collection.addCard(card);
        card.setCollectionId(collection.getId());

        // saving the changes to the database
        repoCard.save(card);
        Collection updatedCollection = repoCollection.save(collection);

        return ResponseEntity.ok(updatedCollection);
    }
}