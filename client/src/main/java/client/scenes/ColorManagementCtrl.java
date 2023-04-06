package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Collection;
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
        Color fontColor = cardFont.getValue();
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
            ColorPreset newColorPreset = new ColorPreset(colorPreset.getId(), colorPreset.getCards(),
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
    }

    /**
     * Updates color of the board
     * when it is changed from the menu
     */
    public void updateBoard() {
        Board newBoard = new Board(currentBoard.getId(), currentBoard.getName(), currentBoard.getCollections(),
                new ArrayList<Double>(){{
                    add(boardFont.getValue().getRed());
                    add(boardFont.getValue().getGreen());
                    add(boardFont.getValue().getBlue());
                    add(boardBackground.getValue().getRed());
                    add(boardBackground.getValue().getGreen());
                    add(boardBackground.getValue().getBlue());
                }});
        currentBoard = newBoard;
        server.send("/app/boards", newBoard, session);
    }

    /**
     * Updates the color of the
     * collections in a board.
     */
    public void updateCollection() {
        for (Collection collection : currentBoard.getCollections()) {
            Collection newCollection = new Collection(collection.getId(), collection.getName(), collection.getBoardId(),
                    collection.getCards(), new ArrayList<Double>(){{
                            add(collectionFont.getValue().getRed());
                            add(collectionFont.getValue().getGreen());
                            add(collectionFont.getValue().getBlue());
                            add(collectionBackground.getValue().getRed());
                            add(collectionBackground.getValue().getGreen());
                            add(collectionBackground.getValue().getBlue());
                        }});
            collection = newCollection;
            server.send("/app/collectionsUpdate", newCollection, session);
        }
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
