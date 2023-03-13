package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import javax.persistence.Entity;

import javax.persistence.Id;

@Entity
public class Card {

    //Nothing to see here, just for testing purposes
    @Id
    private String title;
    private String description;


    @ManyToOne
    @JoinColumn(name = "collection_id", referencedColumnName = "name")
    public Collection collection;

    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collection the collection it the card is in
     */
    public Card(String title, String description, Collection collection) {
        this.title = title;
        this.description = description;
        this.collection = collection;
    }

    /**
     * Dummy constructor.
     */
    public Card() {

    }

    public Card(String title) {
        this.title = title;
    }

    /**
     * get the collection the card is in
     * @return collection
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * sets the collection of the card
     * @param collection the task colum it is in
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
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
     * returns the title of a card
     * @return String
     */
    public String getTitle(){
        return title;
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
     * generates the hashcode for Card
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Human-readable String representation of card
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
