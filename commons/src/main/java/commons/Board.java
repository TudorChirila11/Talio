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

    public Board() {
    }

    public Board(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Board setName(String name) {
        this.name = name;
        return this;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public Board setCollections(List<Collection> collections) {
        this.collections = collections;
        return this;
    }

    public void addCollection(Collection collection) {
        collections.add(collection);
        collection.setBoard(this);
    }

    public void removeCollection(Collection collection) {
        collections.remove(collection);
        collection.setBoard(null);
    }
}
