package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.CollectionRepository;

@RestController
@RequestMapping("/api/reset")
public class ResetController {

    private final CollectionRepository repoCollection;
    private final CardRepository repoCard;
    private final BoardRepository repoBoard;

    /**
     * Controller constructor
     * @param repoCollection repo reference
     * @param repoCard the repository of the cards
     * @param repoBoard the repository of the boards
     */
    public ResetController(CollectionRepository repoCollection,
                           CardRepository repoCard, BoardRepository repoBoard) {
        this.repoCollection = repoCollection;
        this.repoCard = repoCard;
        this.repoBoard = repoBoard;

    }

    /**
     * delete all the items in the database
     * @return responseEntity
     */
    @DeleteMapping(path = { "", "/" })
    public ResponseEntity<Void> deleteAll() {
        repoCollection.deleteAll();
        repoCard.deleteAll();
        repoBoard.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
