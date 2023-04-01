package server.api;

import commons.Card;
import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.SubtaskRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/subtasks")
public class SubtaskController {
    private final SubtaskRepository repo;

    /**
     * class constructor
     * @param repo - reference to a required SubtaskRepository
     */
    SubtaskController(SubtaskRepository repo)
    {
        this.repo = repo;
    }

    /**
     * returns all subtasks
     * @return - a list containing all subtasks
     */
    @GetMapping(path = { "", "/" })
    public List<Subtask> getAll() {
        return repo.findAll();
    }

    /**
     * return subtask, given its id
     * @param id - the required id
     * @return the Subtask object through the response if subtask is found,
     * and a 'Bad Request' otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Optional<Subtask> subtask = repo.findById(id);
        return ResponseEntity.ok(subtask.get());
    }

    /**
     * deletes a subtask from the database
     * @param id - the id of the subtask we want to delete
     * @return void response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * deletes all the subtasks in the database
     * @return void response
     */

    @DeleteMapping(path = {"/", ""})
    public ResponseEntity<Void> deleteAll()
    {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }
    /**
     * adds a subtask object to the database
     * @param subtask - the new subtask we add
     * @return newly created subtask
     */
    @PostMapping(path = {"/", ""})
    public ResponseEntity<Subtask> addSubtask(@RequestBody Subtask subtask)
    {
        Subtask saved = repo.save(subtask);
        return ResponseEntity.ok(saved);
    }

    /**
     * updates a Subtask Object
     * @param newSubtask - new subtask object
     * @param id - id of the subtask we want to change
     * @return edited subtask object as it is now in the database
     */
    @PutMapping("/{id}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable long id, @RequestBody Subtask newSubtask)
    {
        if (!repo.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        Optional<Subtask> subtaskOpt = repo.findById(id);

        Subtask subtask = subtaskOpt.get();

        subtask.setCardId(newSubtask.getCardId());
        subtask.setFinished(newSubtask.getFinished());
        subtask.setIndex(newSubtask.getIndex());
        subtask.setName(newSubtask.getName());

        Subtask saved = repo.save(subtask);
        return ResponseEntity.ok(saved);
    }
}
