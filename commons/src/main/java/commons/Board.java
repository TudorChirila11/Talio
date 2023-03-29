package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;

    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collection> collections = new ArrayList<>();

    /**
     * board constructor with name
     * @param name the name of the board
     * @param tags the tags that the board has
     */
    public Board(String name, List<Tag> tags) {
        this.name = name;
        this.tags = tags;
    }


    /**
     * dummy board constructor
     */
    public Board() {
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
     * get the name of the board
     * @return tags
     */
    public List<Tag> getTags() {
        return tags;
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
     * set the name of the board
     * @param tags the new tags
     * @return self
     */
    public Board setTags(List<Tag> tags) {
        this.tags = tags;
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
     * An equals method for the board data class
     * @param o the object that'll be compared to this
     * @return a boolean representing whether the 2 objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(id, board.id) && Objects.equals(name, board.name) && Objects.equals(collections, board.collections)
                && Objects.equals(tags, board.tags);
    }

    /**
     * A hashcode method for the board data class
     * @return the integer that represents the hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, collections, tags);
    }

    /**
     * A toString method for the subtask data class
     * @return A string representing the whole data class
     */
    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", collections=" + collections +
                ", tags=" + tags +
                '}';
    }
}
