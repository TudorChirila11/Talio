package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.ColorPreset;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
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
        if(currentBoard.getColor() != null && currentBoard.getColor().size() == 6)
        {

            Color val = new Color((6*16) /255.0, (9*16+6) / 255.0,  (11*16+4) / 255.0, 1);
            boardBackground.setValue(val);
            System.out.println(currentBoard.getColor());
            server.send("/app/boards", currentBoard, session);
        }
    }

    /**
     * reset font color for board
     */
    public void resetBoardFontColor()
    {

        if(currentBoard.getColor() != null && currentBoard.getColor().size() == 6)
        {
            Color val = new Color(0D, 0D,  0D, 1);
            boardFont.setValue(val);
            server.send("/app/boards", currentBoard, session);
        }
    }

    /**
     * reset background color for collection
     */
    public void resetCollectionBackgroundColor() {
        if (currentBoard.getCollectionColor() != null && currentBoard.getCollectionColor().size() == 6) {
            Color val = new Color((11 * 16 + 13) / 255.0, (12 * 16 + 13) / 255.0,
                    (13 * 16 + 6) / 255.0, 1);
            collectionBackground.setValue(val);
            server.send("/app/boards", currentBoard, session);
        }
    }

    /**
     * reset font color for collection
     */
    public void resetCollectionFontColor() {
        if (currentBoard.getCollectionColor() != null && currentBoard.getCollectionColor().size() == 6) {
            Color val = new Color(0D, 0D, 0D, 1);
            collectionFont.setValue(val);
            server.send("/app/boards", currentBoard, session);
        }
    }

    /**
     * This method passes the current board to this controller
     * @param board the board that the color is related to
     */
    public void initialize(Board board) {
        currentBoard = board;
    }


    /**
     * Method used to refresh the color preset list
     */
    public void refresh() {
        /*if (colorPreset.equals(new ColorPreset())) {
            cardFont.setValue(Color.BLACK);
            cardBackground.setValue(Color.LIGHTBLUE);
        }*/
    }

    /**
     * Method used to create a new color preset and add
     * it to the database
     */
    public void createColorPreset() {
       /* Color fontColor = cardFont.getValue();
        Color backgroundColor = cardBackground.getValue();
        if(colorPreset.equals(new ColorPreset())) {
            ColorPreset newColorPreset = new ColorPreset(new ArrayList<Double>(){{
                    add(fontColor.getRed());
                    add(fontColor.getGreen());
                    add(fontColor.getBlue());
                    add(backgroundColor.getRed());
                    add(backgroundColor.getGreen());
                    add(backgroundColor.getBlue());
                }}, false);
            server.send("/app/colorPresets", newColorPreset, session);
        } else {
            ColorPreset newColorPreset = new ColorPreset(colorPreset.getId(),
                    new ArrayList<Double>(){{
                        add(fontColor.getRed());
                        add(fontColor.getGreen());
                        add(fontColor.getBlue());
                        add(backgroundColor.getRed());
                        add(backgroundColor.getGreen());
                        add(backgroundColor.getBlue());
                    }}, colorPreset.getDefault());
            colorPreset = newColorPreset;
            server.send("/app/colorPresetsUpdate", newColorPreset, session);
        }
        refresh();
        */
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
        mainCtrl.showBoard(currentBoard);
    }

}
