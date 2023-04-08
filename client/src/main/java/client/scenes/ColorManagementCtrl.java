package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.ColorPreset;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ColorManagementCtrl implements Initializable {

    private ServerUtils server;

    private MainCtrl mainCtrl;

    private Board currentBoard;

    private StompSession session;

    @FXML
    private ColorPicker boardFont;

    @FXML
    private ColorPicker boardBackground;

    @FXML
    private ColorPicker collectionFont;

    @FXML
    private ColorPicker collectionBackground;

    @FXML
    private ColorPicker cardFont;

    @FXML
    private ColorPicker cardBackground;

    @FXML
    private Button resetBoardFont;

    @FXML
    private Button resetBoardBackground;

    @FXML
    private Button resetCollectionFont;

    @FXML
    private Button resetCollectionBackground;

    @FXML
    private Button addPreset;

    @FXML
    private Button editPreset;

    @FXML
    private Button removePreset;

    @FXML
    private Button defaultPreset;

    private ColorPreset colorPreset;

    @FXML
    private MenuButton presetSelector;


    /**
     * ColorManagementCtrl constructor
     * @param server serverUtils ref
     * @param mainCtrl mainCtrl ref
     */
    @Inject
    public ColorManagementCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * A method for starting to listen to a server once the connection has been established
     * @param session the session that is connected to a server that the client is connected to
     */
    public void subscriber(StompSession session) {
        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(this::refresh), session);
        this.session = session;
    }

    /**
     * reset background color for board
     */
    public void resetBoardBackgroundColor()
    {
        Color val = new Color((6*16) /255.0, (9*16+6) / 255.0,  (11*16+4) / 255.0, 1);
        boardBackground.setValue(val);
    }

    /**
     * reset font color for board
     */
    public void resetBoardFontColor()
    {
        Color val = new Color(0D, 0D,  0D, 1);
        boardFont.setValue(val);
    }

    /**
     * reset background color for collection
     */
    public void resetCollectionBackgroundColor() {
        Color val = new Color((11 * 16 + 13) / 255.0, (12 * 16 + 13) / 255.0,
                (13 * 16 + 6) / 255.0, 1);
        collectionBackground.setValue(val);
    }

    /**
     * reset font color for collection
     */
    public void resetCollectionFontColor() {
        Color val = new Color(0D, 0D, 0D, 1);
        collectionFont.setValue(val);
    }

    /**
     * This method passes the current board to this controller
     * @param board the board that the color is related to
     */
    public void initialize(Board board) {
        currentBoard = board;
        refresh();
    }



    /**
     * Method used to refresh the color preset list
     */
    public void refresh() {
        if(currentBoard == null)
            return;
        updateColorFields();
        refreshColorChooser();
    }


    /**
     * refreshes the color chooser
     */
    public void refreshColorChooser()
    {
        presetSelector.getItems().clear();
        List<ColorPreset> presets = server.getPresets(currentBoard.getId());
        colorPreset = null;
        presetSelector.setText("Select...");
        for (ColorPreset c : presets) {

            String name = "Preset " + c.getId();
            if(c.getIsDefault())
                name+=" (Default) ";
            MenuItem i = new MenuItem(name);
            String finalName = name;
            i.setOnAction(event -> {
                presetSelector.setText(finalName);
                colorPreset = c;
                Color font = new Color(c.getColor().get(0), c.getColor().get(1), c.getColor().get(2), 1.0);
                Color background = new Color(c.getColor().get(3), c.getColor().get(4), c.getColor().get(5), 1.0);
                cardFont.setValue(font);
                cardBackground.setValue(background);
            });
            presetSelector.getItems().add(i);
        }
    }

    /**
     * updates the fields related to the color, according to currentBoard
     */
    private void updateColorFields() {
        Object [] colObj = currentBoard.getColor().toArray();
        Double[] col = new Double[colObj.length];
        for(int i = 0; i < 6; ++i)
            col[i] = (double) colObj[i];
        Color c1 = new Color(col[0], col[1], col[2], 1.0);
        Color c2 = new Color(col[3], col[4], col[5], 1.0);

        colObj = currentBoard.getCollectionColor().toArray();
        col = new Double[colObj.length];
        for(int i = 0; i < 6; ++i)
            col[i] = (double) colObj[i];
        Color c3 = new Color(col[0], col[1], col[2], 1.0);
        Color c4 = new Color(col[3], col[4], col[5], 1.0);

        boardBackground.setValue(c2);
        boardFont.setValue(c1);
        collectionBackground.setValue(c4);
        collectionFont.setValue(c3);
    }

    /**
     * Method used to create a new color preset and add
     * it to the database
     */
    public void createColorPreset() {
        List<Double> color = new ArrayList<>(){{
                add(cardFont.getValue().getRed());
                add(cardFont.getValue().getGreen());
                add(cardFont.getValue().getBlue());
                add(cardBackground.getValue().getRed());
                add(cardBackground.getValue().getGreen());
                add(cardBackground.getValue().getBlue());
            }};

        ColorPreset colorPreset1 = new ColorPreset(color, currentBoard.getId(), false);
        colorPreset = server.savePreset(colorPreset1);
        server.send("/app/addPreset", colorPreset, session);
        refreshColorChooser();
    }

    /**
     * edits preset colors
     */
    public void editColorPreset()
    {
        List<Double> color = new ArrayList<>(){{
                add(cardFont.getValue().getRed());
                add(cardFont.getValue().getGreen());
                add(cardFont.getValue().getBlue());
                add(cardBackground.getValue().getRed());
                add(cardBackground.getValue().getGreen());
                add(cardBackground.getValue().getBlue());
            }};
        colorPreset.setColor(color);
        server.send("app/presets", colorPreset, session);
        refreshColorChooser();
    }

    /**
     * Updates color of the board
     * when it is changed from the menu
     */
    public void updateBoard() {
        List<Double> colors = new ArrayList<Double>(){{
                add(boardFont.getValue().getRed());
                add(boardFont.getValue().getGreen());
                add(boardFont.getValue().getBlue());
                add(boardBackground.getValue().getRed());
                add(boardBackground.getValue().getGreen());
                add(boardBackground.getValue().getBlue()); }};

        List<Double> color = new ArrayList<>(){{
                add(collectionFont.getValue().getRed());
                add(collectionFont.getValue().getGreen());
                add(collectionFont.getValue().getBlue());
                add(collectionBackground.getValue().getRed());
                add(collectionBackground.getValue().getGreen());
                add(collectionBackground.getValue().getBlue());
            }};
        currentBoard.setColor(colors);
        currentBoard.setCollectionColor(color);
        server.send("/app/boards", currentBoard, session);
        showCurrentBoard();
    }

    /**
     * deletes this preset
     */
    public void deletePreset()
    {
        if(colorPreset == null) {
            showError("No preset shown!");
            return;
        }
        if(colorPreset.getIsDefault()) {
            showError("Cannot delete default preset! Choose another default first.");
            return;
        }
        List<Card> cards = server.getAllCards();
        for (Card card : cards) {
            if (card.getColorPreset().equals(colorPreset)) {
                showError("Cannot delete this preset! It is currently being used by a card.");
                return;
            }
        }
        server.send("/app/presetsDelete", colorPreset, session);
        refreshColorChooser();
    }

    /**
     *
     */
    public void setDefaultPreset()
    {
        server.send("/app/presetsDefaultChange", colorPreset, session);
        refreshColorChooser();
    }

    /**
     * shows error with the given text
     * @param text - text to be shown in the error
     */
    private void showError(String text) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(text);
        a.show();
    }


    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Goes back to the current board
     */
    public void showCurrentBoard() {
        clearFields();
        mainCtrl.showBoard(currentBoard);
    }

    /**
     * clears the fields of the current screen
     */
    private void clearFields() {
        resetBoardBackgroundColor();
        resetBoardFontColor();
        resetCollectionBackgroundColor();
        resetCollectionFontColor();
        presetSelector.getItems().clear();
    }

}
