package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private boolean isLocked;

    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "boardId", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Collection> collections = new ArrayList<>();


    /**
     * dummy board constructor
     */
    public Board() {
    }

    /**
     * board constructor with id and name
     * @param id the id of the board
     * @param name the name of the board
     */
    public Board(Long id, String name) {
        this.id = id;
        this.name = name;
        this.isLocked = false;
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
<<<<<<< HEAD
     * get the locked status of the board
     * @return isLocked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * set the locked status of the board
     * @param locked the new locked status
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * get the password of the board
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set the password of the board
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * A equals method for the board data class
     * @param o the object to be compared
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
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
     * set the id of the board
     * @param l the id
     */
    public void setId(Long l) {
        this.id = l;
    }

    /**
     * A toString method for the board data class
     * @return a string representation of the board
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
