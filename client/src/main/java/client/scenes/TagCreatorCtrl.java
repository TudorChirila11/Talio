package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

public class TagCreatorCtrl implements Initializable {

    @FXML
    public TextArea tagDescription;

    private Board currentBoard;

    @FXML
    public ColorPicker tagColour;

    @FXML
    public Button createTagButton;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private StompSession session;

    /**
     * Constructor for the CollectionOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public TagCreatorCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
     * referesh method so that line 55 works
     */
    private void refresh() {
    }

    /**
     * creates a Tag
     */
    public void createTag() {
        Color color = tagColour.getValue();
        if(!tagDescription.getText().equals("")) {
            Tag newTag = new Tag(tagDescription.getText(), currentBoard.getId(), new ArrayList<Double>(){{
                    add(color.getRed());
                    add(color.getGreen());
                    add(color.getBlue());
                }});
            server.send("/app/tags", newTag, session);
            mainCtrl.showBoard(currentBoard);
        }
    }

    /**
     * Initialization method for the labels within the collection
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Used to initialize the tagCreator with a board
     * @param board the current board
     */
    public void initialize(Board board) {
        currentBoard = board;
    }

    /**
     * Used to go back to the main board view
     */
    public void goBack() {
        mainCtrl.showBoard(currentBoard);
    }
}