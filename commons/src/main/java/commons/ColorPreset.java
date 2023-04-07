package commons;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "colors")
public class ColorPreset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ElementCollection
    private List<Double> color = new ArrayList<>();

    private Boolean isDefault;

    @Column(name = "boardId")
    private Long boardId;

    /**
     * Color preset constructor
     * @param id the id of the color preset
     * @param cards the cards that the color preset is applied to
     * @param color the color of the color preset
     * @param isDefault if the color selected is default
     */
    public ColorPreset(Long id, List<Card> cards, List<Double> color, Boolean isDefault) {
        this.id = id;
        this.color = color;
        this.isDefault = isDefault;
    }

    /**
     * Color preset constructor
     * @param color the color of the color preset
     * @param isDefault if the color selected is default
     */
    public ColorPreset(List<Double> color, Boolean isDefault) {
        this.color = color;
        this.isDefault = isDefault;
    }

    /**
     * Color preset constructor
     * @param color the color of the color preset
     * @param boardId the id of the current board
     * @param isDefault if the color selected is default
     */
    public ColorPreset(List<Double> color, Long boardId, Boolean isDefault) {
        this.color = color;
        this.boardId = boardId;
        this.isDefault = isDefault;
    }

    /**
     * Dummy constructor
     */
    public ColorPreset() {
    }

    /**
     * id getter
     * @return the color preset id
     */
    public Long getId() {
        return id;
    }


    /**
     * Color getter
     * @return the colors of the color preset
     */
    public List<Double> getColor() {
        return color;
    }

    /**
     * Default getter
     * @return whether the color preset is set as default
     */
    public Boolean getDefault() {
        return isDefault;
    }

    /**
     * Color setter
     * @param color the new color of the color preset
     */
    public void setColor(List<Double> color) {
        this.color = color;
    }

    /**
     * isDefault setter
     * @param isDefault the new default stating whether
     *                  the preset is default or not
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * Equals method
     * @param o object to be compared to
     * @return whether the two objects are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColorPreset)) return false;
        ColorPreset that = (ColorPreset) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getColor(), that.getColor());
    }

    /**
     * hash method
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getColor());
    }

    /**
     * To String method
     * @return the String representation
     */
    @Override
    public String toString() {
        return "ColorPreset{" +
                "id=" + id +
                ", color=" + color +
                '}';
    }
}
