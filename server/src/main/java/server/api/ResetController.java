package server.api;


import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Quote;
import server.database.CardRepository;
import server.database.CollectionRepository;
import server.database.QuoteRepository;

@RestController
@RequestMapping("/api/reset")
public class ResetController {

    private final CollectionRepository repoCollection;
    private final CardRepository repoCard;

    /**
     * Controller constructor
     * @param repoCollection repo reference
     * @param repoCard the repository of the cards
     */
    public ResetController(CollectionRepository repoCollection, CardRepository repoCard) {
        this.repoCollection = repoCollection;
        this.repoCard = repoCard;

    }

    /**
     * delete all the items in the database
     * @return responseEntity
     */
    @DeleteMapping(path = { "", "/" })
    public ResponseEntity<Void> deleteAll() {
        repoCollection.deleteAll();
        repoCard.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
