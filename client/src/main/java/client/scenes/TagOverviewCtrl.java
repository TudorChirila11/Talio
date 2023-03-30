package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable{

    @FXML
    public Button createTagButton;

    private Board currentBoard;

    @FXML
    public VBox tagContainer;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private List<Tag> tagsList;

    /**
     * Constructor for the CollectionOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * this method redirects the user to the tag creation window
     */
    public void createTag() {
        mainCtrl.showTagCreation(currentBoard);
    }

    /**
     * This method passes the current board to this controller
     * @param board the board that the tag overview is related to
     */
    public void initialize(Board board) {
        currentBoard = board;
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
        tagContainer.getChildren().add(new HBox());
        refresh();


        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(this::refresh));
    }

    /**
     * Used to update the tag overview once entered and when the server notifies the client that something has changed
     */
    public void refresh() {
        ListView<HBox> tagListView = new ListView<>();
        tagsList = server.getTags();
        FXMLLoader loader;
        for(Tag tag : tagsList) {
            loader = new FXMLLoader(getClass().getResource("/client/scenes/TagInOverview.fxml"));
            HBox newTag = null;
            try {
                newTag = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TagInOverviewCtrl tagController = loader.getController();
            tagController.setTagText(tag.getName());
            tagController.setColor(tag.getColour());
            tagListView.getItems().add(newTag);
        }
        tagContainer.getChildren().set(0, tagListView);
    }

    /**
     * The method is used to go back to the main board view
     */
    public void goBack() {
        mainCtrl.showBoard(currentBoard);
    }
}
