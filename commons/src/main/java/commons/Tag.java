package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();

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
    public Tag(Long id, String name, Long boardId, List<Card> cards, List<Double> colour){
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
    public Tag(String name, Long boardId, List<Card> cards, List<Double> colour){
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
    public List<Card> getCards() {
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
     * An equals method for the tag entity
     * @param o the object that will be compared to this tag
     * @return a boolean representing whether the object o is equal to this tag or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(boardId, tag.boardId) &&
                Objects.equals(cards, tag.cards) && Objects.equals(colour, tag.colour);
    }

    /**
     * A hashcode method for this entity
     * @return the hashcode of the function
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, boardId, cards, colour);
    }

    /**
     * A toString method for this entity
     * @return a string representing this entity
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", boardId=" + boardId +
                ", cards=" + cards +
                ", colour=" + colour +
                '}';
    }
}
