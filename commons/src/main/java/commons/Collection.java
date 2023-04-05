package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Column(name = "board_id")
    private Long boardId;

    @OrderColumn(name="index")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collectionId", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();


    /**
     * Constructor for Collection
     * @param name - name of the collection
     * @param board - the board to which the collection belongs to
     * @param cards - the cards in the collection
     */
    public Collection(String name, Board board, List<Card> cards) {
        this.name = name;
        this.boardId = board.getId();
        this.cards = cards;
    }

    /**
     * Secondary constructor for use when a new Collection is created
     * @param name - name of the collection
     * @param board - the board to which the collection belongs to
     */
    public Collection(String name, Board board) {
        this.name = name;
        this.boardId = board.getId();
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
     * constructor for the collection class with id
     * @param id the id of the collection used in database
     */
    public Collection(Long id) {
        this.id = id;
    }

    /**
     * the constructor of the collection with id and name
     * @param id the id of the collection
     * @param name the name of the collection
     */
    public Collection(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * the collection constructor
     * @param id the id used by database
     * @param name the name of the collection
     * @param board the board it is on
     */
    public Collection(Long id, String name, Board board) {
        this.id = id;
        this.name = name;
        this.boardId = board.getId();
        this.cards = new LinkedList<>();
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
     * get the board id
     * @return board id
     */
    public Long getBoardId() {
        return boardId;
    }

    /**
     * set the board id
     * @param boardId the id of the board
     * @return self
     */
    public Collection setBoardId(Long boardId) {
        this.boardId = boardId;
        return this;
    }

    /**
     * Getter for the list of cards
     * @return List<Card> - list of all cards present in the
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * get the id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * adds a card to the collection
     * @param card the card to be added
     * @return self
     */
    public Collection addCard(Card card) {
        cards.add(card);
        return this;
    }

    /**
     * remove a card for a collection
     * @param card the card to be removed
     */
    public void removeCard(Card card) {
        assert card != null;
        cards.remove(card);
    }

    /**
     * set ALL the cards of the collection (existing card get removed!)
     * @param cards the cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
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
