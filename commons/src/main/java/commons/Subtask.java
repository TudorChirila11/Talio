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

    @Column(name = "indexInCard")
    private Long index;

    @Column(name = "name")
    private String name;

    @Column(name = "finished")
    private Boolean finished;

    /**
     * class constructor - creates a new Subtask object
     * @param id - this Subtask's id
     * @param cardId - the id of the card this subtask belongs to
     * @param name - this Subtask's name
     * @param finished - this subtask's finished status
     * @param index - this subtask's index inside the card
     */
    public Subtask(Long id, Long cardId, String name, Boolean finished, Long index) {
        this.id = id;
        this.cardId = cardId;
        this.name = name;
        this.index = index;
        this.finished = finished;
    }

    /**
     * constructor that defines a subtask only by name, cardId and index
     * @param cardId - name of the subtask
     * @param name - name of the subtask
     * @param index - name of the subtask
     */
    public Subtask(Long cardId, String name, Long index)
    {
        this.cardId = cardId;
        this.name = name;
        this.index = index;

    }
    /**
     * sets this Subtask's id with given value
     * @param id - new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * sets this Subtask's cardId with given value
     * @param cardId - new cardId
     */
    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    /**
     * sets this Subtask's index with given value
     * @param index - new index
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * sets this Subtask's name with given value
     * @param name - new name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * sets this Subtask's finished attribute with given value
     * @param finished - new value of 'finished'
     */
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    /**
     * @return this object's id value
     */
    public Long getId() {
        return id;
    }

    /**
     * @return this object's cardId value
     */
    public Long getCardId() {
        return cardId;
    }

    /**
     * @return this object's index value
     */
    public Long getIndex() {
        return index;
    }

    /**
     * @return this object's name value
     */
    public String getName() {
        return name;
    }

    /**
     * @return this object's finished value
     */
    public Boolean getFinished() {
        return finished;
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
        return Objects.equals(id, subtask.id) && Objects.equals(cardId, subtask.cardId) && Objects.equals(index, subtask.index) && Objects.equals(name, subtask.name) && Objects.equals(finished, subtask.finished);
    }

    /**
     * A hashcode method for the subtask data class
     * @return the integer that represents the hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, cardId, index, name, finished);
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
                ", index=" + index +
                ", name='" + name + '\'' +
                ", finished=" + finished +
                '}';
    }
}
