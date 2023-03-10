package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Collection {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    public String name;

    @ManyToOne
    @JoinColumn(name = "board_id")
    public Board board;

    @OneToMany(mappedBy = "collection")
    public List<Card> cards;

    /**
     * Constructor for Collection
     * @param name - name of the collection
     * @param board - the board to which the collection belongs to
     * @param cards - the cards in the collection
     */
    public Collection(String name, Board board, List<Card> cards) {
        this.name = name;
        this.board = board;
        this.cards = cards;
    }

    /**
     * Secondary constructor for use when a new Collection is created
     * @param name - name of the collection
     * @param board - the board to which the collection belongs to
     */
    public Collection(String name, Board board) {
        this.name = name;
        this.board = board;
        this.cards = new LinkedList<>();
    }

    /**
     * default constructor (needed for the repository)
     */
    public Collection() {
    }

    /**
     * Constructor with only the id
     * @param name the id of the collection
     */
    public Collection(String name) {
        this.name = name;
    }

    /**
     * Getter for the name of the collection
     * @return String - the name of the collection
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for collection name, in case it needs to be changed
     * @param name - the name of the collection
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the board
     * @return Board - board in which the collection is located
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter for the list of cards
     * @return List<Card> - list of all cards present in the
     */
    public List<Card> getCards() {
        return cards;
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
