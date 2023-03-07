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

    public Card(String title, Collection collection) {
        this.name = title;
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
