package server.api;

import commons.Board;
import commons.Card;
import commons.ColorPreset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ColorPresetControllerTest {

    private TestColorPresetRepository repo;

    private ColorPresetController sut;

    private ColorPreset a, b, c;

    private Board board;

    @BeforeEach
    public void setup()
    {
        board = new Board(1L, "board");
        a = new ColorPreset(1L, 1L, new ArrayList<Double>(), true);
        b = new ColorPreset(2L, 1L, new ArrayList<Double>(), false);
        c = new ColorPreset(3L, 1L, new ArrayList<Double>(), false);
        repo = new TestColorPresetRepository();
        sut = new ColorPresetController(repo);
    }



    @Test
    void addColorPreset() {
        sut.addPreset(a);
        sut.addPreset(b);
        sut.addPreset(c);
    }

    @Test
    void deleteColorPreset() {
        sut.deletePreset(a);
    }

    @Test
    void getAll() {
        List<ColorPreset> ColorPresetList = sut.getAll();
    }

    @Test
    void getAllInBoard() {
        List<ColorPreset> totalColorPresetList = sut.getAllInBoard(board.getId());
    }

    @Test
    void defaultPreset() {
        sut.defaultPreset(b);
    }

    @Test
    void getDefaultInBoard() {
        ColorPreset defaultPreset = sut.getDefaultInBoard(1L);
        assertEquals(new ColorPreset(), defaultPreset);
    }

    @Test
    void deleteAll() {
        sut.deleteAll();
    }

    @Test
    void delete() {
        sut.delete(a.getId());
        sut.delete(123L);
    }
}