package commons;

import javax.persistence.*;
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

    public Tag(Long id, String name, Long boardId){
        this.id = id;
        this.name = name;
        this.boardId = boardId;
    }

    public Tag() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(boardId, tag.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boardId);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", boardId=" + boardId +
                '}';
    }
}
