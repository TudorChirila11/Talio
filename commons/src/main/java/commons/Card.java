package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    /**
     * @param title used by the user to see what card is what
     * constructor of the card class
     */
    public Card(String title) {
        this.title = title;
    }

    /**
     * gets the id of the card
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * to change the card id
     * @param id of the card
     * @return self
     */
    public Card setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * gets the title of the card
     * @return id
     */
    public String getTitle() {
        return title;
    }

    /**
     * to change the card title
     * @param title of the card
     * @return self
     */
    public Card setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * the equals methode is used to test if two cards are the same
     * @param o other object
     * @return boolean true if same false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getId() == card.getId() && Objects.equals(getTitle(), card.getTitle());
    }

    /**
     * generate a hashcode of the object
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }

    /**
     * the to string methode
     * @return string of the object
     */
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
