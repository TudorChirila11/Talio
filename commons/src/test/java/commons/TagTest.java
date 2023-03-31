package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    Tag a;
    Tag b;
    Tag c;
    Board d;
    @BeforeEach
    void setup()
    {
        d = new Board();
        a = new Tag(324L, "tag", d.getId(), new ArrayList<Card>(), new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)));
        b = new Tag("tag", d.getId(), new ArrayList<Card>(), new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)));
        c = new Tag("tag", d.getId(), new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)));
    }

    @Test
    void testConstructor5params()
    {
        assertEquals("tag", a.getName());
        assertEquals( new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)) , a.getColour());
        assertEquals(d.getId(), a.getBoardId());
        assertEquals(new ArrayList<Card>(), a.getCards());
        assertEquals(324L, a.getId());
    }

    @Test
    void testConstructor4params()
    {
        assertEquals("tag", b.getName());
        assertEquals( new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)) , b.getColour());
        assertEquals(d.getId(), b.getBoardId());
        assertEquals(new ArrayList<Card>(), b.getCards());
    }

    @Test
    void testConstructor3params()
    {
        assertEquals("tag", c.getName());
        assertEquals( new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)) , c.getColour());
        assertEquals(d.getId(), c.getBoardId());
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
    void testEquals() {
        Tag e = a;
        assertEquals(e, a);
        assertSame(e, a);
        Tag f = new Tag(324L, "tag", d.getId(), new ArrayList<Card>(), new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)));
        assertEquals(f, a);
        assertNotEquals(f, b);
        assertNotEquals(f, c);
        assertEquals(f, e);
        assertNotEquals(f, d);
    }

    @Test
    void testHash() {
        Tag e = a;
        Tag f = new Tag(324L, "tag", d.getId(), new ArrayList<Card>(), new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0)));
        assertEquals(e.hashCode(), a.hashCode());
        assertEquals(f.hashCode(), a.hashCode());
        assertNotEquals(b.hashCode(), a.hashCode());
        assertNotEquals(d.hashCode(), a.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Tag{" +
                "id=" + a.getId() +
                ", name='" + a.getName() + '\'' +
                ", boardId=" + a.getBoardId() +
                ", cards=" + a.getCards() +
                ", colour=" + a.getColour() +
                '}', a.toString());
    }
}