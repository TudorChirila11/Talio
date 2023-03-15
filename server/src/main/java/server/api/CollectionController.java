
package server.api;

import java.util.List;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

//    /**
//     * adding a card to a collection
//     * @param id of the collection
//     * @param card the card to be added
//     * @return the Response Entity
//     */
//    @PostMapping(path = { "/addCardTo/{id}" })
//    public ResponseEntity<Collection> add(@PathVariable("id") long id, @RequestBody Card card) {
//        System.out.println(card);
//        System.out.println("id is equal to = " + id);
//
//        if (!repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Collection c = repo.findById(id).orElse(null);
//        if (c == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        c.addCard(card);
//        Collection updatedCollection = repo.save(c);
//        return ResponseEntity.ok(updatedCollection);
//    }
    @Transactional
    @PostMapping(path = {"/CardAddTo/{id_card}/{id_collection}"})
    public ResponseEntity<Collection> add(@PathVariable("id_card") long id_card,
                                          @PathVariable("id_collection") long id_collection) {

        // checking if both card and collection exist
        if (!repoCollection.existsById(id_collection) || !repoCard.existsById(id_card)) {
            return ResponseEntity.badRequest().build();
        }

        // getting the card and collection
        Collection collection = repoCollection.findById(id_collection).orElse(null);
        Card card = repoCard.findById(id_card).orElse(null);

        // not sure why but this check is important (if you know tell me -Teun)
        if (collection == null || card == null) {
            return ResponseEntity.notFound().build();
        }

        collection.addCard(card);
        Collection updatedCollection = repoCollection.save(collection);
        return ResponseEntity.ok(updatedCollection);





    }
}