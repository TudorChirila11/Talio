
package server.api;

import java.util.List;
import java.util.Optional;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
     * This method receives and distributes collections between clients
     * @param c the collection that the server has received and will send to all the clients on the network
     * @return a collection
     */
    @MessageMapping("/collections") // /app/collections
    @SendTo("/topic/update")
    public Collection addCollection(Collection c){
        add(c);
        return c;
    }

    /**
     * This method receives and distributes collections between clients
     * @param c the collection that the server has received and will send to all the clients on the network
     * @return a collection
     */
    @MessageMapping("/collectionsDelete") // /app/collectionsDelete
    @SendTo("/topic/update")
    public Collection deleteCollection(Collection c){
        delete(c.getId());
        return c;
    }

    /**
     * This method receives and distributes collections between clients
     * @param s a string that is needed for the method to work
     * @return a collection
     */
    @MessageMapping("/collectionsDeleteAll") // /app/collectionsDelete
    @SendTo("/topic/update")
    public Collection deleteAllCollections(Collection s){
        deleteAll();
        return s;
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
    ////TODO currently error-prone, see below why
    @DeleteMapping("{collectionId}/deleteCard/{index}/")
    public ResponseEntity<Collection> deleteCardAtPosition
    (@PathVariable long collectionId, @PathVariable int index) {


        Optional<Collection> collectionOpt = repoCollection.findById(collectionId);

        System.out.println("collection found?");
        // the collection is not found
        if (collectionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("yes, remove");
        Collection collection = collectionOpt.get();
        List<Card> cards = collection.getCards();

        // making sure that the position is filled
        assert cards.size() > 0;
        assert (index >= 0 && index < cards.size());

        Card c = cards.get(index);
        c.setIndex(null); ///TODO makes index null, you have to be careful
        cards.remove(index);
        for(int i = index; i <cards.size(); ++i)
        {
            cards.get(i).setIndex((long) i);
            repoCard.save(cards.get(i));
        }
        // store new list
        Collection updatedCollection = repoCollection.save(collection);
        return ResponseEntity.ok(updatedCollection);

    }

    /**
     * remove card from collection 'collectionId', index 'index', and add it to 'newCollection', index 'newIndex'
     * @param collectionId - old collection
     * @param index - old index
     * @param newCollection - new Collection
     * @param newIndex - new index
     * @return new collection
     */
    @GetMapping("{collectionId}/{index}/{newCollection}/{newIndex}")
    public ResponseEntity<Collection> switchCardPosition
    (@PathVariable long collectionId, @PathVariable int index, @PathVariable long newCollection, @PathVariable int newIndex) {
        Optional<Collection> collectionOpt = repoCollection.findById(collectionId);
        Optional<Collection> collectionOpt2 = repoCollection.findById(newCollection);
        System.out.println("collection found?");
        // the collection is not found
        if (collectionOpt.isEmpty() || collectionOpt2.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("yes, remove");
        Collection collection = collectionOpt.get();
        List<Card> cardsOld = collection.getCards();

        Collection collection2 = collectionOpt2.get();
        List<Card> cardsNew = collection2.getCards();

        // making sure that the position is filled
        assert cardsOld.size() > 0;
        assert cardsNew.size() > 0;
        assert (index >= 0 && index < cardsOld.size());
        assert (newIndex >= 0 && newIndex < cardsNew.size());

        Card c = cardsOld.get(index);
        cardsOld.remove(index);
        for(int i = index; i < cardsOld.size(); ++i)
        {
            cardsOld.get(i).setIndex((long) i);
            repoCard.save(cardsOld.get(i));
        }
        c.setCollectionId(newCollection);
        cardsNew.add(newIndex, c);
        for(int i = newIndex; i <cardsNew.size() ;++i)
        {
            cardsNew.get(i).setIndex((long) i);
            repoCard.save(cardsNew.get(i));
        }
        // store new list
        Collection updatedCollection1 = repoCollection.save(collection);
        Collection updatedCollection2 = repoCollection.save(collection2);
        Card updatedCard = repoCard.save(c);
        return ResponseEntity.ok(updatedCollection2);
    }

//    /{collectionId}/{cardId}/{position}

    /**
     * the insert method set a card in a specific position
     * @param collectionId the id of the collection to add to
     * @param cardId the id of the card to be inserted
     * @param index the index it should be in the end state
     * @return the Response Entity
     */
    @PostMapping("/{collectionId}/{cardId}/{index}")
    public ResponseEntity<Collection> insert
    (@PathVariable Long collectionId, @PathVariable Long cardId, @PathVariable int index ) {
        Optional<Collection> collectionOpt = repoCollection.findById(collectionId);
        Optional<Card> cardOpt = repoCard.findById(cardId);

        // the collection or card is not found
        if (collectionOpt.isEmpty() || cardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Collection collection = collectionOpt.get();
        List<Card> cards = collection.getCards();
        Card theCard = cardOpt.get();

        // removing the old position from the database
        if (theCard.getCollectionId() != null) {
            Collection oldCollection = repoCollection.findById(theCard.getCollectionId()).orElse(null);
            if (oldCollection != null) {
                oldCollection.getCards().remove(theCard);
                theCard.setCollectionId(null);
            }
        }

        boolean successful = true;
        // insert the card in the right spot
        cards.add(index, theCard);
        for(int i = index; i < cards.size() ; ++i) {
            cards.get(i).setIndex((long) i);
            repoCard.save(cards.get(i));
        }
        theCard.setCollectionId(collectionId);
        // save the new info
        Collection updatedCollection = repoCollection.save(collection);
        return ResponseEntity.ok(updatedCollection);
    }

    /**
     * the put API for the collection object
     * @param id the id of the collection to update
     * @param newCollection the data the object should have
     * @return the responseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Collection> updateCollection(@PathVariable("id") long id, @RequestBody Collection newCollection) {

        // test if we have the collection in the database
        if (!repoCollection.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // find the collection in the database
        Collection collectionInDatabase = repoCollection.findById(id).get();

        // set the property's
        collectionInDatabase.setName(newCollection.getName());
        collectionInDatabase.setBoardId(newCollection.getBoardId());

        // remove the cards
        collectionInDatabase.getCards().clear();

        storeTheCards(id, newCollection, collectionInDatabase);

        // store the collection
        Collection theSavedCollection = repoCollection.save(collectionInDatabase);

        return ResponseEntity.ok(theSavedCollection);
    }

    /**
     * this methode checks and stores the cards in the cardrepo
     * @param collectionId the id of the collection the cards belong to
     * @param newCollection the new collection
     * @param collectionInDatabase the old collection
     */
    private void storeTheCards(long collectionId, Collection newCollection, Collection collectionInDatabase) {
        // loop over the new cards
        for (Card newCard : newCollection.getCards()) {
            // set the right collection collectionId
            if (newCard.getCollectionId() == null || !newCard.getCollectionId().equals(collectionId)) {
                newCard.setCollectionId(collectionId);
            }

            // do we need to store the card in the card repo first
            Card tempCard = newCard;
            if (newCard.getId() == null || !repoCard.existsById(newCard.getId())) {
                tempCard = repoCard.save(newCard);
            }

            collectionInDatabase.addCard(tempCard);
        }
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
        // removing the card from the old collection
        if(card.getCollectionId() !=null ) {
            Collection oldCollection = repoCollection.getById(card.getCollectionId());
            oldCollection.removeCard(card);
            repoCollection.save(oldCollection);
        }

        // adding the card to the collection
        collection.addCard(card);
        card.setCollectionId(collection.getId());
        card.setIndex((long) collection.getCards().size() - 1);

        // saving the changes to the database
        repoCard.save(card);
        Collection updatedCollection = repoCollection.save(collection);

        return ResponseEntity.ok(updatedCollection);
    }
}