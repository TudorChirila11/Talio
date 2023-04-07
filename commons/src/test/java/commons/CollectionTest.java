package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
