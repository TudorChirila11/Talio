package commons;

import javax.persistence.*;

@Entity
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id")
    private Long cardId;

    private String name;

    /**
     * class constructor - creates a new Subtask object
     * @param id - this Subtask's id
     * @param cardId - the id of the card this subtask belongs to
     * @param name - this Subtask's name
     */
    public Subtask(Long id, Long cardId, String name) {
        this.id = id;
        this.cardId = cardId;
        this.name = name;
    }

    /**
     * Default constructor
     */
    public Subtask() {

    }
}
