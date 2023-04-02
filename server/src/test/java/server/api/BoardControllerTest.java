package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;

public class BoardControllerTest {
    private BoardController sut;
    private TestBoardRepository repo;

    private Board a, b, c;

    @BeforeEach
    public void setup()
    {
        a = new Board(1L,"board1");
        b = new Board(2L,"board2");
        c = new Board(3L,"board3");
        repo = new TestBoardRepository();
        sut = new BoardController(repo);
    }

    @Test
    void addBoardWebsocketMethod() {
        sut.addBoard(c);
        assertTrue(repo.existsById(c.getId()));
    }

    @Test
    void deleteBoardByIdWebsocketMethod() {
        repo.save(c);
        Board id = sut.deleteBoard(c);
        assertEquals(c, id);
    }

    @Test
    void deleteAllBoardsWebsocketMethod() {
        saveAll();
        sut.deleteAll(a);
        assertEquals(0, repo.findAll().size());
    }

    @Test
    void getAllBoardsFromTheDatabase() {
        repo.save(c);
        repo.save(b);
        ArrayList<Board> newc = new ArrayList<>();
        newc.add(c);
        newc.add(b);
        assertEquals(newc, sut.getAll());
    }

    private void saveAll() {
        repo.save(a);
        repo.save(b);
        repo.save(c);
    }
    @Test
    void getBoardByID() {
        saveAll();
        var res = sut.getById(b.getId());
        assertEquals(b, res.getBody());
    }

    @Test
    void getBoardByIDNotFound() {
        saveAll();
        var res = sut.getById(4L);
        assertEquals(BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void deleteBoardByIDNotFound() {
        saveAll();
        var res = sut.delete(4L);
        assertEquals(NOT_FOUND, res.getStatusCode());
    }



}
