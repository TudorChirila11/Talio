package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void testBoardConstructorWithName() {
        Board board = new Board("Board name");
        assertEquals("Board name", board.getName());
        assertFalse(board.isLocked());
    }

    @Test
    public void testBoardConstructorWithIdAndName() {
        Board board = new Board(1L, "Board name");
        assertEquals(1L, board.getId().longValue());
        assertEquals("Board name", board.getName());
        assertFalse(board.isLocked());
    }

    @Test
    public void testBoardConstructorWithIdNameAndColor() {
        List<Double> color = Arrays.asList(1.0, 1.0, 1.0);
        Board board = new Board(1L, "Board name", color);
        assertEquals(1L, board.getId().longValue());
        assertEquals("Board name", board.getName());
        assertEquals(color, board.getColor());
        assertFalse(board.isLocked());
    }

    @Test
    public void testBoardConstructorWithIdNameCollectionsAndColor() {
        List<Double> color = Arrays.asList(1.0, 1.0, 1.0);
        Collection collection = new Collection(1L, "Collection name");
        List<Collection> collections = Arrays.asList(collection);
        Board board = new Board(1L, "Board name", collections, color);
        assertEquals(1L, board.getId().longValue());
        assertEquals("Board name", board.getName());
        assertEquals(collections, board.getCollections());
        assertEquals(color, board.getColor());
        assertFalse(board.isLocked());
    }

    @Test
    public void testSetName() {
        Board board = new Board("Board name");
        board.setName("New board name");
        assertEquals("New board name", board.getName());
    }

    @Test
    public void testAddAndRemoveCollection() {
        Board board = new Board("Board name");
        Collection collection = new Collection("Collection name");
        board.addCollection(collection);
        assertTrue(board.getCollections().contains(collection));
        board.removeCollection(collection);
        assertFalse(board.getCollections().contains(collection));
    }

    @Test
    public void testIsLocked() {
        Board board = new Board("Board name");
        assertFalse(board.isLocked());
        board.setLocked(true);
        assertTrue(board.isLocked());
    }

    @Test
    public void testGetAndSetPassword() {
        Board board = new Board("Board name");
        assertNull(board.getPassword());
        board.setPassword("password");
        assertEquals("password", board.getPassword());
    }


    @Test
    public void testSetColor() {
        Board board = new Board();
        List<Double> color = new ArrayList<>(Arrays.asList(1.0, 0.0, 0.0));
        board.setColor(color);
        assertEquals(color, board.getColor());
    }

    @Test
    public void testSetCollectionColor() {
        Board board = new Board();
        List<Double> color = new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0));
        board.setCollectionColor(color);
        assertEquals(color, board.getCollectionColor());
    }

    @Test
    public void testSetCollections() {
        Board board = new Board();
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Collection 1"));
        collections.add(new Collection("Collection 2"));
        board.setCollections(collections);
        assertEquals(collections, board.getCollections());
    }

    @Test
    public void testToString() {
        Board board = new Board();
        List<Double> color = new ArrayList<>(Arrays.asList(0.5, 0.5, 0.5));
        board.setColor(color);
        List<Double> collectionColor = new ArrayList<>(Arrays.asList(0.0, 0.0, 1.0));
        board.setCollectionColor(collectionColor);
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Collection 1"));
        collections.add(new Collection("Collection 2"));
        board.setCollections(collections);

        assertTrue(board.toString().contains("Collection 1"));
    }
}

