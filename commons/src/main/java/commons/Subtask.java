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

    public Subtask(Long id, Long cardId, String name) {
        this.id = id;
        this.cardId = cardId;
        this.name = name;
    }
}
