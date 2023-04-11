package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.springframework.http.HttpStatus.*;

import static org.junit.jupiter.api.Assertions.*;

//CHECKSTYLE:OFF
class CardControllerTest {

    private TestCardRepository cardRepo;

    private CardController sut;

    private Card a, b, c;

    @BeforeEach
    public void setup()
    {
        a = new Card(1L,"title", "description", 21L, 4L);
        b = new Card(2L,"ti", "description", 23L, 4L);
        c = new Card(3L,"title33", "description", 23L, 4L);
        cardRepo = new TestCardRepository();

        sut = new CardController(cardRepo);
    }
    @Test
    void addingCardWebsocketMethod() {
        sut.addCard(c);
        assertTrue(cardRepo.existsById(c.getId()));
    }

    @Test
    void deleteCardByIdWebsocketMethod() {
        cardRepo.save(c);
        Long id = sut.deleteCard(c.getId());
        assertEquals(c.getId(), id);
    }

    @Test
    void deleteAllCardsWebsocketMethod() {
        saveAll();
        sut.deleteAllCards(a);
        assertEquals(0, cardRepo.findAll().size());
    }

    @Test
    void getAllCardsFromTheDatabase() {
        cardRepo.save(c);
        cardRepo.save(b);
        ArrayList<Card> newc = new ArrayList<>();
        newc.add(c);
        newc.add(b);
        assertEquals(newc, sut.getAll());
    }

    @Test
    void getCardByIdCardExists() {
        saveAll();
        var res = sut.getById(b.getId());
        assertEquals(b, res.getBody());
    }

    @Test
    void getCardByIdCardDoesNotExist()
    {
        saveAll();
        var res = sut.getById(123);
        assertEquals(BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void getCardsByGivenTitleCardsExist() {
        saveAll();
        var res = sut.getByTitle(c.getTitle());
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(c);
        assertEquals(expected, res.getBody());
    }

    @Test
    void getCardsByGivenTitleCardsNotExist()
    {
        saveAll();
        var res = sut.getByTitle("random title");
        assertEquals(NOT_FOUND, res.getStatusCode());
    }

    @Test
    void updateCardCardExists() {
        cardRepo.save(a);
        cardRepo.save(b);
        var res = sut.updateCard(b.getId(), c);
        assertNotEquals(b.getId(), c.getId());
        assertEquals(b.getTitle(), c.getTitle());
    }

    @Test
    void updateCardCardNotExist()
    {
        cardRepo.save(a);
        var res = sut.updateCard(b.getId(), a);
        assertEquals(NOT_FOUND, res.getStatusCode());
    }
    @Test
    void getCardsByIdOfCollection() {
        saveAll();
        var res = sut.getCardsByIdOfCollection(23L);
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(c);
        expected.add(b);
        assertEquals(expected, res.getBody());
    }

    @Test
    void deleteCardByIdCardExists() {
        cardRepo.save(c);
        cardRepo.save(a);
        sut.delete(c.getId());
        var expected = new ArrayList<Card>();
        expected.add(a);
        assertEquals(expected, cardRepo.findAll());
    }

    @Test
    void deleteCardByIdCardNotExist()
    {
        cardRepo.save(a);
        var res = sut.delete(b.getId());
        assertEquals(NOT_FOUND, res.getStatusCode());
    }

    @Test
    void deleteAllCards() {
        saveAll();
        sut.deleteAll();
        assertEquals(0, cardRepo.findAll().size());
    }

    @Test
    void addCardObject() {
        var response = sut.add(c);
        assertEquals(OK, response.getStatusCode());

    }

    /**
     * saves all given cards in the database
     * method exists to avoid boilerplate
     */
    private void saveAll()
    {
        cardRepo.save(a);
        cardRepo.save(b);
        cardRepo.save(c);
    }

    @Test
    void getUpdates() {
        sut.getUpdates();
    }
}