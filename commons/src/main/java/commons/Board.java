package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collection> collections = new ArrayList<>();


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
        collection.setBoard(this);
    }

    /**
     * remove a collection from the board
     * @param collection the collection to be removed
     */
    public void removeCollection(Collection collection) {
        collections.remove(collection);
        collection.setBoard(null);
    }
}
