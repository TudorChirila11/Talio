package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import javax.persistence.Entity;


import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private ColorPreset colorPreset;

    @Column(name = "collection_id")
    private Long collectionId;


    /***
     * this is this card's index inside its collection.
     * IMPORTANT CONVENTION TO RESPECT: each card's value inside a collection needs to be a distinct number from 0 to collection.getCards().size() - 1
     */
    @Column(name = "index")
    private Long index;

    @OrderColumn(name="indexInCard")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardId", cascade = CascadeType.ALL)
    private List<Subtask> subtasks = new ArrayList<>();


    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collectionId the id of collection
     * @param index the index of this card inside its collection
     * @param subtasks - list of subtasks
     * @param colorPreset - the color preset that the card uses
     */
    public Card(String title, String description, Long collectionId, Long index, List<Subtask> subtasks, ColorPreset colorPreset) {
        this.title = title;
        this.description = description;
        this.collectionId = collectionId;
        this.index = index;
        this.subtasks = subtasks;
        this.colorPreset = colorPreset;
    }


    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collectionId the id of collection
     * @param index the index of this card inside its collection
     * @param subtasks - list of subtasks
     */
    public Card(String title, String description, Long collectionId, Long index, List<Subtask> subtasks) {
        this.title = title;
        this.description = description;
        this.collectionId = collectionId;
        this.index = index;
        this.subtasks = subtasks;
    }

    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collection the id of collection
     * @param index the id of the card inside its collection
     * @param colorPreset the color preset that the card uses
     */
    public Card(String title, String description, Collection collection, Long index, ColorPreset colorPreset) {
        this.title = title;
        this.description = description;
        this.collectionId = collection.getId();
        this.index = index;
        this.subtasks = new ArrayList<>();
        this.colorPreset = colorPreset;
    }

    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param description the description for the card
     * @param collection the id of collection
     * @param index the id of the card inside its collection
     */
    public Card(String title, String description, Collection collection, Long index) {
        this.title = title;
        this.description = description;
        this.collectionId = collection.getId();
        this.index = index;
        this.subtasks = new ArrayList<>();
    }

    /**
     * the constructor of the card class
     * @param id the id of the card
     * @param title the name users can see
     * @param description the description for the card
     * @param collectionId the id of collection
     * @param index the id of the card inside its collection
     */
    public Card(Long id, String title, String description, Long collectionId, Long index) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.collectionId = collectionId;
        this.index = index;
        this.subtasks = new ArrayList<>();
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
     * @return this object's subtasks value
     */
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    /**
     * sets this object's subtask list
     * @param subtasks - new subtasks list
     */
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
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
     * @return this object's index value
     */
    public Long getIndex() {
        return index;
    }

    /**
     * sets this object's index value
     * @param index
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * This is the getter for the color preset
     * @return the current color preset
     */
    public ColorPreset getColorPreset() {
        return colorPreset;
    }

    /**
     * This is the setter for the color preset
     * @param colorPreset the color preset that'll replace the current color preset
     */
    public  void setColorPreset(ColorPreset colorPreset) {
        this.colorPreset = colorPreset;
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
