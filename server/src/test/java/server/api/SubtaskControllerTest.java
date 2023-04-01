package server.api;

import commons.Card;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.SubtaskRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

//CHECKSTYLE:OFF
class SubtaskControllerTest {

    private TestSubtaskRepository repo;

    private SubtaskController sut;

    private Subtask a, b, c;

    @BeforeEach
    public void setup()
    {
        a = new Subtask(1L, 123L, "name", true, 134L);
        b = new Subtask(2L, 123123L, "n12ame", false, 1124L);
        c = new Subtask(3L, 1213L, "name4241", true, 1132L);
        repo = new TestSubtaskRepository();
        sut = new SubtaskController(repo);
    }

    /**
     * method for avoiding boilerplate
     */
    private void addAll()
    {
        repo.save(a);
        repo.save(b);
        repo.save(c);
    }
    @Test
    void getAllTestTest() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(a);
        subtasks.add(b);
        repo.save(a);
        repo.save(b);
        assertEquals(subtasks, sut.getAll());
    }

    @Test
    void getByIdTest() {
        addAll();
        assertEquals(b, sut.getById(2).getBody());
    }

    @Test
    void getByIdNotFoundTest()
    {
        assertEquals(NOT_FOUND, sut.getById(1).getStatusCode());
    }
    @Test
    void deleteByIdTest() {
        addAll();
        sut.deleteById(3);
        ArrayList<Subtask> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);
        assertEquals(expected, repo.findAll());
    }

    @Test
    void deleteByIdNotFoundTest()
    {
        assertEquals(NOT_FOUND, sut.deleteById(1).getStatusCode());
    }

    @Test
    void deleteAllTest() {
        addAll();
        sut.deleteAll();
        assertEquals(new ArrayList<Subtask>(), repo.findAll());
    }

    @Test
    void addSubtaskTest() {
        sut.addSubtask(c);
        assertTrue(repo.existsById(c.getId()));
    }

    @Test
    void updateSubtaskTest() {
        Long oldId = a.getId();
        repo.save(a);
        sut.updateSubtask(a.getId(), b);
        assertEquals(oldId, a.getId());
        assertEquals(a.getName(), b.getName());
    }

    @Test
    void updateSubtaskNotFoundTest()
    {
        assertEquals(NOT_FOUND, sut.updateSubtask(1, a).getStatusCode());
    }
}