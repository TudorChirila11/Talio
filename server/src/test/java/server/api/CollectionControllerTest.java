package server.api;

import commons.Board;
import commons.Card;
import commons.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//CHECKSTYLE:OFF

public class CollectionControllerTest {

    private TestCollectionRepository repoCollection;
    private TestCardRepository repoCard;

    private CollectionController sut;

    private Card a, b, c;
    private Collection bob, fred;

    private Board board;

    @BeforeEach
    public void setup() {
        a = new Card(1L,"title", "description", 21L, 4L);
        b = new Card(2L,"ti", "description", 23L, 4L);
        c = new Card(3L,"title33", "description", 23L, 4L);

        board = new Board();
        board.setId(3L);

        bob = new Collection(23L, "collection bob", board);
        fred = new Collection(21L, "collection fred", board);


        bob.addCard(b);
        bob.addCard(c);
        fred.addCard(a);

        repoCard = new TestCardRepository();
        repoCollection = new TestCollectionRepository();


        sut = new CollectionController(repoCollection, repoCard);
    }

//    /**
//     * This method receives and distributes collections between clients
//     * @param c the collection that the server has received and will send to all the clients on the network
//     * @return a collection
//     */
//    @MessageMapping("/collections") // /app/collections
//    @SendTo("/topic/update")
//    public Collection addCollection(Collection c){
//        add(c);
//        return c;
//    }

    @Test
    void addingCollectionWebsocketMethod() {
        sut.addCollection(fred);
        assertTrue(repoCollection.existsById(fred.getId()));
    }


//    /**
//     * This method receives and distributes collections between clients
//     * @param c the collection that the server has received and will send to all the clients on the network
//     * @return a collection
//     */
//    @MessageMapping("/collectionsDelete") // /app/collectionsDelete
//    @SendTo("/topic/update")
//    public Collection deleteCollection(Collection c){
//        delete(c.getId());
//        return c;
//    }

    @Test
    void deleteCollectionByIdWebsocketMethod() {
        repoCollection.save(fred);
        Collection res = sut.deleteCollection(fred);
        assertEquals(fred, res);
    }


//    /**
//     * This method receives and distributes collections between clients
//     * @param board board to delete collections from.
//     * @return a collection
//     */
//    @MessageMapping("/collectionsDeleteAll") // /app/collectionsDelete
//    @SendTo("/topic/update")
//    public Board deleteAllCollections(Board board){
//
//        deleteCollectionsByBoardId(board.getId());
//        return board;
//    }

    @Test
    void deleteAllCollectionsWebsocketMethod() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        sut.deleteAllCollections(board);
        assertEquals(0, repoCollection.findAll().size());
    }





//    /**
//     * Hardcoded mapping all collections
//     * @return List of collections objectss
//     */
//    @GetMapping(path = { "", "/" })
//    public List<Collection> getAll() {
//        return repoCollection.findAll();
//    }

    @Test
    void getAllCollectionsFromTheDatabase() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        ArrayList<Collection> newc = new ArrayList<>();

        newc.add(fred);
        newc.add(bob);
        assertEquals(newc, sut.getAll());
    }



//    /**
//     //     * get the collections by the id of board
//     //     * @param id the id of the board
//     //     * @return the collections
//     //     */
//    @GetMapping("/{id}/ofBoard")
//    public ResponseEntity<List<Collection>> getCollectionsByBoardID(@PathVariable long id) {
//        List<Collection> allCards = repoCollection.findAll();
//        List<Collection> res = new ArrayList<>();
//        for (Collection c : allCards) {
//            if (c.getId() != null && c.getBoardId() == id) {
//                res.add(c);
//            }
//        }
//        return ResponseEntity.ok(res);
//    }

    @Test
    void testGetCollectionsByBoardID() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        ArrayList<Collection> newc = new ArrayList<>();

        assertEquals(2, sut.getCollectionsByBoardID(board.getId()).getBody().size());
    }

//
//    /**
//     * delete all the collections in the database
//     * @return responseEntity
//     */
//    @DeleteMapping(path = { "", "/" })
//    public ResponseEntity<Void> deleteAll() {
//        repoCollection.deleteAll();
//        return ResponseEntity.noContent().build();
//    }

    @Test
    void testDeleteAll() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        sut.deleteAll();
        assertEquals(0, repoCollection.findAll().size());

    }

//    /**
//     * add a card to a collection methode
//     * @param idCard the id of the card
//     * @param idCollection the id of the collection
//     * @return responseEntity
//     */
//    @PostMapping(path = {"/CardAddTo/{id_card}/{id_collection}"})
//    public ResponseEntity<Collection> add(@PathVariable("id_card") long idCard,
//                                          @PathVariable("id_collection") long idCollection) {
//
//        // checking if both card and collection exist
//        if (!repoCollection.existsById(idCollection) || !repoCard.existsById(idCard)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        // getting the card and collection
//        Collection collection = repoCollection.findById(idCollection).orElse(null);
//        Card card = repoCard.findById(idCard).orElse(null);
//
//        // not sure why but this check is important (if you know tell me -Teun)
//        if (collection == null || card == null) {
//            return ResponseEntity.notFound().build();
//        }
//        // removing the card from the old collection
//        if(card.getCollectionId() !=null ) {
//            Collection oldCollection = repoCollection.getById(card.getCollectionId());
//            oldCollection.removeCard(card);
//            repoCollection.save(oldCollection);
//        }
//
//        // adding the card to the collection
//        collection.addCard(card);
//        card.setCollectionId(collection.getId());
//        card.setIndex((long) collection.getCards().size() - 1);
//
//        // saving the changes to the database
//        repoCard.save(card);
//        Collection updatedCollection = repoCollection.save(collection);
//
//        return ResponseEntity.ok(updatedCollection);
//    }

    @Test
    void testAddCardToCollectionCardAndCollectionExist() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        repoCard.save(a);
        repoCard.save(b);
        repoCard.save(c);

        ResponseEntity<Collection> response = sut.add(1L, 23L);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(3, response.getBody().getCards().size());
        assertEquals(23L, response.getBody().getId());
    }

    @Test
    void testAddCardToCollectionCardOrCollectionNotExist() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        repoCard.save(a);
        repoCard.save(b);
        repoCard.save(c);

        ResponseEntity<Collection> response = sut.add(1L, 25L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = sut.add(5L, 23L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }



//    /**
//     * Hardcoded mapping all collections
//     * @return List of collections objectss
//     */
//    @GetMapping(path = { "", "/" })
//    public List<Collection> getAll() {
//        return repoCollection.findAll();
//    }

    @Test
    void testFindAll() {
        repoCollection.save(fred);
        repoCollection.save(bob);
        List<Collection> ar = new ArrayList<>();
        ar.add(fred);
        ar.add(bob);

        assertEquals(ar, sut.getAll());

    }
}


//CHECKSTYLE:ON