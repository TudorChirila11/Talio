package server.api;

import commons.Board;
import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagControllerTest {

    private TestTagRepository repo;

    private TagController sut;

    private Tag a, b, c;

    private Board board;

    @BeforeEach
    public void setup()
    {
        board = new Board(1L, "board");
        a = new Tag(1L, "tag", board.getId(), new ArrayList<Long>(){}, new ArrayList<Double>());
        b = new Tag(2L, "tag", board.getId(), new ArrayList<Long>(), new ArrayList<Double>());
        c = new Tag(3L, "tag", board.getId(), new ArrayList<Long>(), new ArrayList<Double>());
        repo = new TestTagRepository();
        sut = new TagController(repo);
    }



    @Test
    void addTag() {
        sut.addTag(a);
        sut.addTag(b);
        sut.addTag(c);
    }

    @Test
    void deleteTag() {
        sut.deleteTag(a);
    }

    @Test
    void editTag() {
        b.setName("woah, I am a diferrent tag!");
        sut.editTag(b);
    }

    @Test
    void deleteAllTags() {
        sut.deleteAllTags(new Tag());
    }

    @Test
    void getAll() {
        List<Tag> tagList = sut.getAll();
    }

    @Test
    void getAllInBoard() {
        List<Tag> totalTagList = sut.getAllInBoard(board.getId());
    }

    @Test
    void getAllInCard() {
        Card card = new Card(123L, null, null, null, null);
        a.setCards(new ArrayList<Long>(){{
            add(card.getId());
        }});
        sut.updateTag(a.getId(), a);
        List<Tag> cardTagList = sut.getAllInCard(123L);
        assertEquals(a, sut.getAllInCard(123L).get(0));
    }

    @Test
    void deleteAll() {
        sut.deleteAll();
    }

    @Test
    void delete() {
        sut.delete(a.getId());
        sut.delete(123L);
    }

    @Test
    void updateTag() {
        b.setName("test test thing");
        sut.updateTag(b.getId(), b);
        sut.updateTag(123L, new Tag());
    }


}