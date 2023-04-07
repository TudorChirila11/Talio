package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collection> collections = new ArrayList<>();

    @ElementCollection
    private List<Double> color = new ArrayList<>();

    @ElementCollection
    private List<Double> collectionColor = new ArrayList<>();


    /**
     * dummy board constructor
     */
    public Board() {
    }

    /**
     * board constructor with id, name and color
     * @param id the id of the board
     * @param name the name of the board
     * @param color the color of the board
     */
    public Board(Long id, String name, List<Double> color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * board constructor with id, name, collections, and color
     * @param id the id of the board
     * @param name the name of the board
     * @param collections the collections in the board
     * @param color the color of the board
     */
    public Board(Long id, String name, List<Collection> collections, List<Double> color) {
        this.id = id;
        this.name = name;
        this.collections = collections;
        this.color = color;
    }

    /**
     * board constructor with id and name
     * @param id the id of the board
     * @param name the name of the board
     */
    public Board(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * board constructor with name
     * @param name the name of the board
     */
    public Board(String name) {
        this.name = name;
    }

    /**
     * get the id of the board
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * get the name of the board
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the board
     * @param name the new name
     * @return self
     */
    public Board setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Color getter method
     * @return the board's color
     */
    public List<Double> getColor() {
        return color;
    }

    /**
     * Color setter method
     * @param color the board's new color
     */
    public void setColor(List<Double> color) {
        this.color = color;
    }

    /**
     * Collection color getter method
     * @return the collection color
     */
    public List<Double> getCollectionColor() {
        return collectionColor;
    }

    /**
     * Collection color setter method
     * @param collectionColor the new collection color
     */
    public void setCollectionColor(List<Double> collectionColor) {
        this.collectionColor = collectionColor;
    }

    /**
     * get the collections on the board
     * @return collections
     */
    public List<Collection> getCollections() {
        return collections;
    }

    /**
     * set the collections on the board
     * @param collections a list
     * @return self
     */
    public Board setCollections(List<Collection> collections) {
        this.collections = collections;
        return this;
    }

    /**
     * add a collection to the board
     * @param collection the new collection
     */
    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    /**
     * remove a collection from the board
     * @param collection the collection to be removed
     */
    public void removeCollection(Collection collection) {
        collections.remove(collection);
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

    /**
     * set the id of the board
     * @param l the id
     */
    public void setId(Long l) {
        this.id = l;
    }
}
