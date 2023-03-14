package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long board_id;


    public long getBoard_id() {
        return board_id;
    }

    public Board setId(long id) {
        this.board_id = id;
        return this;
    }
}
