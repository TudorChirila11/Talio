package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Board {
    //Nothing to see here, just for testing purposes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
}
