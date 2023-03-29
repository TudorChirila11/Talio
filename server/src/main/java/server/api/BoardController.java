package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;

    /**
     * Controller constructor
     *
     * @param repo repo reference
     */
    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * Hardcoded mapping all cards
     *
     * @return List of cards objects
     */
    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * getById method for a Board with path var
     *
     * @param id of Card
     * @return a response Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }


    /**
     * this will delete the board
     *
     * @param id the id / name of the board
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * deletes all the Cards in the database
     *
     * @return responseEntity
     */
    @DeleteMapping(path = {"", "/"})
    public ResponseEntity<Void> deleteAll() {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }

    /**
     * Initalization of a board
     *
     * @param board a new board obj
     * @return the Response Entity
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }


}

