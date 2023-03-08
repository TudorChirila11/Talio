package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;

    /**
     * @param title used by the user to see what card is what
     * @param description the description used to describe the task
     * constructor of the card class
     */
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
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
     * getter for the description of a card
     * @return a description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter for the description of a card
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Equals method for the card class
     * @param obj potentially another card
     * @return true/false
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hashcode equality for card
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Human readable String representation of card
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
