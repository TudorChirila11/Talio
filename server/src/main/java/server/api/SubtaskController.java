package server.api;

import commons.Subtask;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.SubtaskRepository;

import java.util.ArrayList;
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
     * This method receives and distributes subtasks between clients
     * @param subtask the subtask that the server has received and will send to all the clients on the network
     * @return a subtask
     */
    @MessageMapping("/subtasks") // /app/subtasks
    @SendTo("/topic/update")
    public Subtask add(Subtask subtask){
        addSubtask(subtask);
        return subtask;
    }

    /**
     * This method receives and distributes subtasks between clients
     * @param id the subtask's id that the server has received and will send to all the clients on the network
     * @return a subtask
     */
    @MessageMapping("/subtasksDelete") // /app/subtasks
    @SendTo("/topic/update")
    public Long delete(Long id){
        deleteById(id);
        return id;
    }

    /**
     * This method receives and distributes collections between clients
     * @param s a string that is needed for the method to work
     * @return a collection
     */
    @MessageMapping("/subtasksDeleteAll") // /app/collectionsDelete
    @SendTo("/topic/update")
    public Subtask deleteAll(Subtask s){
        deleteAll();
        return s;
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


    /**
     * returns all subtasks of specific card
     * @param cardId - id of the card
     * @return List with all required subtasks
     */
    @GetMapping("/getFromCard/{cardId}")
    public ResponseEntity<List<Subtask>> getSubtasksFromCard(@PathVariable long cardId)
    {
        List<Subtask> subtasks = repo.findAll(Sort.sort(Subtask.class).by(Subtask::getIndex));
        List<Subtask> response = new ArrayList<>();
        for(Subtask c: subtasks)
            if(c.getCardId() == cardId)
                response.add(c);
        return ResponseEntity.ok(response);
    }

    /**
     * stores a list of subtasks in a specific card
     * @param cardId - the id of the card these subtasks belong to
     * @param subtasks - subtasks list
     * @return void response
     */
    @PostMapping("storeInCard/{cardId}")
    public ResponseEntity<Void> storeSubtasksInCard(@PathVariable long cardId, @RequestBody List<Subtask> subtasks)
    {
        for(int i = 0 ; i < subtasks.size(); ++i)
        {
            subtasks.get(i).setCardId(cardId);
            subtasks.get(i).setFinished(false); ///TODO to be changed when tick button ready
            subtasks.get(i).setIndex((long) i);
            repo.save(subtasks.get(i));
        }
        return ResponseEntity.noContent().build();
    }
}
