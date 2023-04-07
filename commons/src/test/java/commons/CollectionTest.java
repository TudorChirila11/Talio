package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CollectionTest {
    private Collection collection;

    @BeforeEach
    public void setUp() {
        collection = new Collection("test collection");
    }

    @Test
    public void constructorOne() {
        List<Card> cards = List.of(new Card("test card"));
        Collection collection = new Collection("test collection" , new Board("hey"),cards);
        assertEquals("test collection", collection.getName());
    }

    @Test
    public void setCards() {
        List<Card> cards = List.of(new Card("test card"));
        collection.setCards(cards);
        assertEquals(cards, collection.getCards());
    }

    @Test
    public void constructorTwo() {
        Collection collection = new Collection("test collection" , new Board("hey"));
        assertNotNull(collection);
    }

    @Test
    public void constructorID(){
        Collection collection = new Collection(1L);
        assertEquals(1L, collection.getId());
    }

    @Test
    public void constructorIDAndName(){
        Collection collection = new Collection(1L, "test collection");
        assertEquals("test collection", collection.getName());
    }

    @Test
    public void testCollectionWithAllParams() {
        Long id = 1L;
        String name = "Test Collection";
        Board board = new Board("Test Board");
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Test Card"));
        List<Double> color = new ArrayList<>();
        color.add(0.1);
        Collection collection = new Collection(id, name, board.getId(), cards, color);
        assertEquals(id, collection.getId());
        assertEquals(name, collection.getName());
        assertEquals(board.getId(), collection.getBoardId());
        assertEquals(cards, collection.getCards());
        assertEquals(color, collection.getColor());
    }

    @Test
    public void testCollectionConstructor() {
        Long id = 1L;
        String name = "My Collection";
        Long boardId = 2L;
        List<Card> cards = new ArrayList<>();
        List<Double> color = new ArrayList<>();

        Collection collection = new Collection (name, new Board(), cards, color);

        assertNotNull(collection);
    }

    @Test
    public void setColor() {
        List<Double> color = new ArrayList<>();
        color.add(0.1);
        collection.setColor(color);
        assertEquals(color, collection.getColor());
    }



}
