package server.api;

import commons.ColorPreset;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.ColorPresetRepository;

import java.util.List;

@RestController
@RequestMapping("/api/presets")
public class ColorPresetController {

    private final ColorPresetRepository repo;

    /**
     * Controller constructor
     *
     * @param repo repo reference
     */
    public ColorPresetController(ColorPresetRepository repo) {
        this.repo = repo;
    }

    /**
     * This method receives and distributes presets between clients
     * @param p the preset that the server has received and will send to all the clients on the network
     * @return a board
     */
    @MessageMapping("/presets") // /app/presets
    @SendTo("/topic/update")
    public ColorPreset addPreset(ColorPreset p) {

        add(p);
        return p;
    }


    /**
     * This method receives and distributes presets between clients
     * @param p the preset that the server has received and will send to all the clients on the network
     * @return a board
     */
    @MessageMapping("/presetsDelete") // /app/presetsDelete
    @SendTo("/topic/update")
    public ColorPreset deletePreset(ColorPreset p) {
        delete(p.getId());
        return p;
    }


    /**
     * This method receives and distributes presets between clients
     * @param p the preset that the server has received and will send to all the clients on the network
     * @return a board
     */
    @MessageMapping("/presetsDefaultChange") // /app/presetsDelete
    @SendTo("/topic/update")
    public ColorPreset defaultPreset(ColorPreset p) {
        p.setIsDefault(true);
        ColorPreset p1 = getDefaultInBoard(p.getBoardId());
        p1.setIsDefault(false);
        add(p1);
        add(p);
        return p;
    }

    /**
     * Hardcoded mapping all presets
     *
     * @return List of boards objects
     */
    @GetMapping(path = {"", "/"})
    public List<ColorPreset> getAll() {
        return repo.findAll();
    }


    /**
     * Hardcoded mapping all tags
     * @param boardId the id which will be used to get only the tags that the client needs
     * @return List of tags objects
     */
    @GetMapping(path = {"/{id}"})
    public List<ColorPreset> getAllInBoard(@PathVariable("id") long boardId) {
        return repo.findByBoardId(boardId);
    }


    /**
     * Hardcoded mapping all tags
     * @param boardId the id which will be used to get only the tags that the client needs
     * @return List of tags objects
     */
    @GetMapping(path = {"/default/{id}"})
    public ColorPreset getDefaultInBoard(@PathVariable("id") long boardId) {
        return repo.findByIsDefaultAndBoardId(true, boardId);
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
     * Method for adding a color preset
     * @param preset a new board obj
     * @return the Response Entity
     */
    @PostMapping(path = {"/", ""})
    public ResponseEntity<ColorPreset> add(@RequestBody ColorPreset preset) {
        ColorPreset saved = repo.save(preset);
        return ResponseEntity.ok(saved);
    }
}
