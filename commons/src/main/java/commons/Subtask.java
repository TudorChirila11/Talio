package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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
     * the equals methode
     * @param obj other object
     * @return boolean true iff same
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * generate the hashcode
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * to string methode
     * @return string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
