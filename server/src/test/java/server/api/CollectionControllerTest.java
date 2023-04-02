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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @Test
    void getCollectionById() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(fred, sut.getById(fred.getId()).getBody());
    }

    @Test
    void getCollectionByIdNotFound() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(BAD_REQUEST, sut.getById(100L).getStatusCode());
    }

    @Test
    void getCardsByCollectionId() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(1, sut.getCardsByCollectionId(fred.getId()).getBody().size());
    }

    @Test
    void deleteCardsAtIndexBiggerCollection() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(bob, sut.deleteCardAtPosition(bob.getId(), 0).getBody());
    }
    @Test
    void getCardsByCollectionIdNotFound() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(NOT_FOUND, sut.getCardsByCollectionId(100L).getStatusCode());
    }

    @Test
    void deleteCardAtPositionCollectionNotFound() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(NOT_FOUND, sut.deleteCardAtPosition(100L, 0).getStatusCode());
    }

    @Test
    void deleteCardAtPosition() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(fred, sut.deleteCardAtPosition(fred.getId(), 0).getBody());
    }


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


    @Test
    void testFindAll() {
        repoCollection.save(fred);
        repoCollection.save(bob);
        List<Collection> ar = new ArrayList<>();
        ar.add(fred);
        ar.add(bob);

        assertEquals(ar, sut.getAll());

    }

    @Test
    void updateCollectionNotFound() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(NOT_FOUND, sut.updateCollection(100L, fred).getStatusCode());
    }

    @Test
    void updateCollection() {
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(fred, sut.updateCollection(fred.getId(), fred).getBody());
    }

    @Test
    void switchCardPositionBetweenCollections() {
        fred.addCard(new Card());
        repoCollection.save(fred);
        repoCollection.save(bob);

        Card c = sut.switchCardPosition(fred.getId(), 0, bob.getId(), 0).getBody();
        Card test = sut.getById(bob.getId()).getBody().getCards().get(0);

        assertEquals(c, test);
    }

    @Test
    void switchCardPositionBetweenCollectionsNotFound() {
        fred.addCard(new Card());
        repoCollection.save(fred);
        repoCollection.save(bob);

        assertEquals(NOT_FOUND, sut.switchCardPosition(fred.getId(), 0, 100L, 0).getStatusCode());
    }

    @Test
    void insertCardIntoCollection(){
        repoCollection.save(fred);
        repoCollection.save(bob);
        repoCard.save(a);
        repoCard.save(b);
        repoCard.save(c);
        assertEquals(fred, sut.insert(fred.getId(), a.getId(), 0).getBody());
    }

    @Test
    void insertCardIntoCollectionNotFound(){
        repoCollection.save(fred);
        repoCollection.save(bob);
        repoCard.save(a);
        repoCard.save(b);
        repoCard.save(c);
        assertEquals(NOT_FOUND, sut.insert(fred.getId(), 100L, 0).getStatusCode());
    }

    @Test
    void storeTheCards(){
        repoCollection.save(fred);
        repoCollection.save(bob);
        repoCard.save(a);
        repoCard.save(b);
        repoCard.save(c);
        Collection temp = new Collection();
        temp.addCard(a);
        temp.addCard(b);
        temp.addCard(c);
        temp.addCard(new Card());
        sut.storeTheCards(fred.getId(),temp, fred);
        assertEquals(fred, sut.getById(fred.getId()).getBody());
    }

    @Test
    void deleteNotFound(){
        repoCollection.save(fred);
        repoCollection.save(bob);
        assertEquals(NOT_FOUND, sut.delete(100L).getStatusCode());
    }

}

//CHECKSTYLE:ON