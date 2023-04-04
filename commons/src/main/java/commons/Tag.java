package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "board_id")
    private Long boardId;

    @ElementCollection
    private List<Long> cards = new ArrayList<>();

    @ElementCollection
    private List<Double> colour = new ArrayList<>();

    /**
     * A constructor to be used in most places
     * @param id the id of the tag
     * @param name the text of the tag
     * @param boardId the board that the tag belongs to
     * @param cards the cards that use this tag
     * @param colour the colour of the tag and the tag's text
     */
    public Tag(Long id, String name, Long boardId, List<Long> cards, List<Double> colour){
        this.id = id;
        this.name = name;
        this.boardId = boardId;
        this.cards = cards;
        this.colour = colour;
    }

    /**
     * Default constructor to be used in most places
     * @param name the text of the tag
     * @param boardId the board that the tag belongs to
     * @param cards the cards that use this tag
     * @param colour the colour of the tag and the tag's text
     */
    public Tag(String name, Long boardId, List<Long> cards, List<Double> colour){
        this.name = name;
        this.boardId = boardId;
        this.cards = cards;
        this.colour = colour;
    }

    /**
     * Default constructor to be used in most places
     * @param name the text of the tag
     * @param boardId the board that the tag belongs to
     * @param colour the colour of the tag and the tag's text
     */
    public Tag(String name, Long boardId, List<Double> colour){
        this.name = name;
        this.boardId = boardId;
        this.colour = colour;
    }

    /**
     * Dummy constructor necessary for the entity to work
     */
    public Tag() {
    }

    /**
     * A getter for the id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * A getter of the tag text
     * @return the tag text
     */
    public String getName() {
        return name;
    }

    /**
     * a getter for the board id
     * @return board id
     */
    public Long getBoardId() {
        return boardId;
    }

    /**
     * a getter for the tag and the tag's text colour
     * @return the colour of the tag
     */
    public List<Double> getColour() {
        return colour;
    }

    /**
     * a getter for the tag colour
     * @return the colour of the tag
     */
    public List<Long> getCards() {
        return cards;
    }

    /**
     * @param name a
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * a setter for the tag colour and the tag's text colour
     * @param colour the new colour of the tag
     */
    public void setColour(List<Double> colour) {
        this.colour = colour;
    }

    /**
     * A setter method for the cards that will be using this tag
     * @param cards the cards that will be using this tag
     */
    public void setCards(List<Long> cards) {
        this.cards = cards;
    }

    /**
     * An equals method for the tag entity
     * @param obj the object that will be compared to this tag
     * @return a boolean representing whether the object o is equal to this tag or not
     */
    @Override
    public boolean equals(Object obj) {return EqualsBuilder.reflectionEquals(this, obj);}

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
