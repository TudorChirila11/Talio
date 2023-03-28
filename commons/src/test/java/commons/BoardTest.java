package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board("test board");
    }

    @Test
    public void testGetId() {
        Assertions.assertNull(board.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("test board", board.getName());
    }

    @Test
    public void testSetName() {
        board.setName("new name");
        assertEquals("new name", board.getName());
    }

    @Test
    public void testGetCollections() {
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("test collection"));
        board.setCollections(collections);

        assertEquals(collections, board.getCollections());
    }

    @Test
    public void testAddCollection() {
        Collection collection = new Collection("test collection");
        board.addCollection(collection);

        assertEquals(collection, board.getCollections().get(0));
    }

    @Test
    public void testRemoveCollection() {
        Collection collection = new Collection("test collection");
        board.addCollection(collection);
        board.removeCollection(collection);

        Assertions.assertTrue(board.getCollections().isEmpty());
    }

    @Test
    public void testEqualsAndHashCode() {
        Board board1 = new Board("board1");
        Board board2 = new Board("board2");
        assertNotEquals(board1, board2);
        assertNotEquals(board1.hashCode(), board2.hashCode());
    }

    @Test
    public void hasToString() {
        Board board = new Board("board1");
        board.addCollection(new Collection("collection1"));

        String expected = board.toString();
        assertTrue(expected.contains(board.getName()));
        assertTrue(expected.contains("\n"));
        assertTrue(expected.contains("collections"));
    }
}
