package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import javax.persistence.Entity;

import javax.persistence.Id;

@Entity
public class Card {

    //Nothing to see here, just for testing purposes

    @Id
    public String name;

    @ManyToOne
    @JoinColumn(name = "collection_id", referencedColumnName = "name")
    public Collection collection;

    /**
     * the constructor of the card class
     * @param title the name users can see
     * @param collection the collection it the card is in
     */
    public Card(String title, Collection collection) {
        this.name = title;
        this.collection = collection;
    }

    /**
     * get the collection the card is in
     * @return collection
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * sets the name to
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the name of the card
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * sets the collection of the card
     * @param collection the task colum it is in
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    /**
     * the equals methode
     * @param obj the other object
     * @return boolean true iff same
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * generates the hashcode
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * the to string methode
     * @return string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
