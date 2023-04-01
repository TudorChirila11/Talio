package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Subtask sb, sb2, sb3;
    @BeforeEach
    void setup()
    {
        sb = new Subtask(1L, 4L, "name", true, 3L);
        sb2 = new Subtask(1L, 4L, "name", true, 3L);
        sb3 = new Subtask(3L, 1L, "name", true, 3L);
    }
    @Test
    void setIdTest() {
        sb.setId(123L);
        assertEquals(123L, sb.getId());
    }

    @Test
    void setCardIdTest() {
        sb.setCardId(1412L);
        assertEquals(1412L, sb.getCardId());
    }

    @Test
    void setIndexTest() {
        sb.setIndex(213L);
        assertEquals(213L, sb.getIndex());
    }

    @Test
    void emptyConstructorTest()
    {
        Subtask sb = new Subtask();
        assertNotNull(sb);
    }
    @Test
    void setName() {
        sb.setName("hello");
        assertEquals("hello", sb.getName());
    }

    @Test
    void setFinished() {
        sb.setFinished(false);
        assertEquals(false, sb.getFinished());
    }

    @Test
    void getId() {
        assertEquals(1L, sb.getId());
    }

    @Test
    void getCardId() {
        assertEquals(4L, sb.getCardId());
    }

    @Test
    void getIndex() {
        assertEquals(3L, sb.getIndex());
    }

    @Test
    void getName() {
        assertEquals("name", sb.getName());
    }

    @Test
    void getFinished() {
        assertEquals(true, sb.getFinished());
    }

    @Test
    void testEqualsSameObject() {
        Subtask same = sb;
        assertEquals(same, sb);
    }
    @Test
    void testEqualsDifferentData() {
        assertNotEquals(sb, sb3);
    }

    @Test
    void testEqualsDiffObjectsSameData() {
        assertEquals(sb, sb2);
    }

    @Test
    void testHashCode() {
        assertEquals(sb.hashCode(), sb2.hashCode());
    }

    @Test
    void testToString() {
        String res = sb.toString();
        assertTrue(res.contains("Subtask"));
        assertTrue(res.contains("id=1"));
        assertTrue(res.contains("cardId=4"));
        assertTrue(res.contains("finished=true"));
        assertTrue(res.contains("index=3"));
    }
}