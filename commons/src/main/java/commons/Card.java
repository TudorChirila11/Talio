package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
@Entity
public class Card {

    //Nothing to see here, just for testing purposes

    @Id
    public String name;

    @Column (nullable = false)
    public String collection_id;

    @ManyToOne
    @JoinColumn(name = "collection_id", referencedColumnName = "name")
    public Collection collection;

}
