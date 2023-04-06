package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Random;

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
     * This method receives and distributes board between clients
     * @param b the board that the server has received and will send to all the clients on the network
     * @return a board
     */
    @MessageMapping("/boards") // /app/boards
    @SendTo("/topic/update")
    public Board addBoard(Board b) {
        add(b);
        return b;
    }


    /**
     * This method receives and distributes boards between clients
     * @param b the b that the server has received and will send to all the clients on the network
     * @return a board
     */
    @MessageMapping("/boardsDelete") // /app/boards
    @SendTo("/topic/update")
    public Board deleteBoard(Board b){
        delete(b.getId());
        return b;
    }

    /**
     * This method deletes all boards.
     * @param board any given board
     * @return a board
     */
    @MessageMapping("/allBoardsDelete") // /app/allBoardsDelete
    @SendTo("/topic/update")
    public Board deleteAll(Board board){
        deleteAll();
        return board;
    }

    /**
     * Hardcoded mapping all cards
     *
     * @return List of boards objects
     */
    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * This method generates a random admin key
     * @return a response entity
     */
    @GetMapping("/adminKey")
    public ResponseEntity<String> getAdminKey() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        String adminKey = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(adminKey);
        return ResponseEntity.ok(adminKey);
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
     * deletes all the Boards in the database
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
    @PostMapping(path = {"/", ""})
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }


}
