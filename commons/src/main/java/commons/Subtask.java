package commons;

import javax.persistence.*;
import java.util.Objects;

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

    /**
     * An equals method for the subtask data class
     * @param o the object that'll be compared to this
     * @return a boolean representing whether the 2 objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(id, subtask.id) && Objects.equals(cardId, subtask.cardId) && Objects.equals(name, subtask.name);
    }

    /**
     * A hashcode method for the subtask data class
     * @return the integer that represents the hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, cardId, name);
    }

    /**
     * A toString method for the subtask data class
     * @return A string representing the whole data class
     */
    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", cardId=" + cardId +
                ", name='" + name + '\'' +
                '}';
    }
}
