package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ColorPresetTest {
    private ColorPreset colorPreset;

    @BeforeEach
    public void setUp() {
        colorPreset = new ColorPreset();
    }

    @Test
    public void testConstructorWithIdCardsColorIsDefault() {
        Long id = 1L;
        List<Card> cards = Arrays.asList(new Card("test card"));
        List<Double> color = Arrays.asList(0.1, 0.2, 0.3);
        Boolean isDefault = true;

        ColorPreset colorPreset = new ColorPreset(id, cards, color, isDefault);

        assertEquals(id, colorPreset.getId());
        assertEquals(color, colorPreset.getColor());
        assertEquals(isDefault, colorPreset.getIsDefault());
    }

    @Test
    public void testConstructorWithColorIsDefault() {
        List<Double> color = Arrays.asList(0.1, 0.2, 0.3);
        Boolean isDefault = true;

        ColorPreset colorPreset = new ColorPreset(color, isDefault);

        assertEquals(color, colorPreset.getColor());
        assertEquals(isDefault, colorPreset.getIsDefault());
    }

    @Test
    public void testConstructorWithColorBoardIdIsDefault() {
        List<Double> color = Arrays.asList(0.1, 0.2, 0.3);
        Long boardId = 2L;
        Boolean isDefault = true;

        ColorPreset colorPreset = new ColorPreset(color, boardId, isDefault);

        assertEquals(color, colorPreset.getColor());
        assertEquals(boardId, colorPreset.getBoardId());
        assertEquals(isDefault, colorPreset.getIsDefault());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        List<Card> cards = Arrays.asList(new Card("test card"));
        List<Double> color = Arrays.asList(0.1, 0.2, 0.3);
        Boolean isDefault = true;
        Long boardId = 2L;


        colorPreset.setColor(color);
        colorPreset.setIsDefault(isDefault);
        colorPreset.setBoardId(boardId);

        assertEquals(color, colorPreset.getColor());
        assertEquals(isDefault, colorPreset.getIsDefault());
        assertEquals(boardId, colorPreset.getBoardId());
    }

    @Test
    public void testEqualsAndHashCode() {
        List<Double> color = Arrays.asList(0.1, 0.2, 0.3);
        Boolean isDefault = true;

        ColorPreset colorPreset1 = new ColorPreset(color, isDefault);
        ColorPreset colorPreset2 = new ColorPreset(color, isDefault);

        assertTrue(colorPreset1.equals(colorPreset2));
        assertTrue(colorPreset2.equals(colorPreset1));
        assertEquals(colorPreset1.hashCode(), colorPreset2.hashCode());
    }

    @Test
    void testToString() {
        ColorPreset preset = new ColorPreset(1L, null, List.of(1.0, 0.5, 0.0), false);
        String expected = "ColorPreset{id=1, color=[1.0, 0.5, 0.0], isDefault=false, boardId=null}";
        assertEquals(expected, preset.toString());
    }
}