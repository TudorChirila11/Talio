package commons;

import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card a;
    @BeforeEach
    void setup()
    {
        a = new Card(324L, "card", "description", 1254L, 234L);
    }

    @Test
    void testConstructor4params()
    {
        Card b = new Card("t", "c", 2L, 3L);
        assertEquals("t", b.getTitle());
        assertEquals("c", b.getDescription());
    }

    @Test
    void testConstructorWithCollection()
    {
        Collection c = new Collection("hello");
        Card b = new Card("t", "c", c, 2L);
        assertEquals("t", b.getTitle());
    }

    @Test
    void testConstructorEmpty()
    {
        Card b = new Card();
        assertNotNull(b);
    }

    @Test
    void testConstructorTitle()
    {
        Card b = new Card("title");
        assertEquals("title", b.getTitle());
    }

    @Test
    void testConsctructorTD()
    {
        Card b = new Card("ttt", "ddd");
        assertEquals("ttt", b.getTitle());
    }
    @Test
    void getCollectionId() {
        assertEquals(1254L, a.getCollectionId());
    }

    @Test
    void setCollectionId() {
        a.setCollectionId(543L);
        assertEquals(543L, a.getCollectionId());
    }

    @Test
    void getId() {
        assertEquals(324L, a.getId());
    }

    @Test
    void setTitle() {
        a.setTitle("new title12");
        assertEquals("new title12", a.getTitle());
    }

    @Test
    void getTitle() {
        assertEquals("card", a.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("description", a.getDescription());
    }

    @Test
    void setDescription() {
        a.setDescription("other description");
        assertEquals("other description", a.getDescription());
    }

    @Test
    void getIndex() {
        assertEquals(234L, a.getIndex());
    }

    @Test
    void setIndex() {
        a.setIndex(314L);
        assertEquals(314L, a.getIndex());
    }

    @Test
    void testEquals1() {
        Card b = a;
        assertEquals(a, b);
    }

    @Test
    void testEquals2()
    {
        Card b = new Card(324L, "card34", "description", 1254L, 234L);
        assertNotEquals(a, b);
    }

    @Test
    void testEquals3()
    {
        Card c = new Card(324L, "card", "description", 1254L, 234L);
        assertEquals(a, c);
    }

    @Test
    void testHashCode() {
        Card b = new Card(324L, "card", "description", 1254L, 234L);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        String res = a.toString();
        assertTrue(res.contains("Card"));
        assertTrue(res.contains("id=324"));
        assertTrue(res.contains("title=card"));
        assertTrue(res.contains("description=description"));
        assertTrue(res.contains("collectionId=1254"));
        assertTrue(res.contains("index=234"));
        assertEquals(res, a.toString());
    }
}