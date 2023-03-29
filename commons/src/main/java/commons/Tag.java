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
    private List<Tag> cards = new ArrayList<>();

    @ElementCollection
    private List<Double> colour = new ArrayList<>();

    /**
     * @param name a
     * @param boardId a
     * @param colour a
     */
    public Tag(String name, Long boardId, List<Double> colour){
        this.name = name;
        this.boardId = boardId;
        this.colour = colour;
    }

    /**
     *
     */
    public Tag() {
    }

    /**
     * @return a
     */
    public Long getId() {
        return id;
    }

    /**
     * @return a
     */
    public String getName() {
        return name;
    }

    /**
     * @return a
     */
    public Long getBoardId() {
        return boardId;
    }

    /**
     * @return a
     */
    public List<Double> getColour() {
        return colour;
    }

    /**
     * @param name a
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param colour a
     */
    public void setColour(List<Double> colour) {
        this.colour = colour;
    }

    /**
     * @param o a
     * @return a
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(boardId, tag.boardId);
    }

    /**
     * @return a
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, boardId);
    }

    /**
     * @return a
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", boardId=" + boardId +
                '}';
    }
}
