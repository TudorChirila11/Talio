package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import javax.persistence.Entity;


import javax.persistence.Id;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "collection_id")
    private Long collectionId;


    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collectionId the id of collection
     */
    public Card(String title, String description, Long collectionId) {
        this.title = title;
        this.description = description;
        this.collectionId = collectionId;
    }

    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collection the id of collection
     */
    public Card(String title, String description, Collection collection) {
        this.title = title;
        this.description = description;
        this.collectionId = collection.getId();
    }

    /**
     * Dummy constructor.
     */
    public Card() {

    }

    /**
     * the constructor of the card with a title
     * @param title the title of the card
     */
    public Card(String title) {
        this.title = title;
    }

    /**
     * constructor for the card class
     * @param title the title of the card
     * @param description the text the gives more info about the task
     */
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * get the id of the collection id is in
     * @return the id of collection
     */
    public Long getCollectionId() {
        return collectionId;
    }

    /**
     * set the id of the collection
     * @param collectionId the id
     * @return the card
     */
    public Card setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
        return this;
    }

    /**
     * get the id
     * @return id
     */
    public Long getId() {
        return id;
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
     * @param description the description of the cord
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
