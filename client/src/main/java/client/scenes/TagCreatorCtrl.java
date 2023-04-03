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
import javafx.scene.text.Text;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

public class TagCreatorCtrl implements Initializable {

    @FXML
    public TextArea tagDescription;

    @FXML
    public Text resultUpdate;

    @FXML
    public ColorPicker tagTextColour;

    private Board currentBoard;

    @FXML
    public ColorPicker tagColour;

    @FXML
    public Button createTagButton;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private StompSession session;

    private Tag tag;

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
        this.session = session;
    }

    /**
     * referesh method so that line 55 works
     */
    public void refresh() {
        if (tag.equals(new Tag())) {
            tagDescription.setText("");
            tagColour.setValue(Color.WHITE);
            tagTextColour.setValue(Color.BLACK);
            resultUpdate.setText("Successfully added a tag!");
        } else {
            createTagButton.setText("update tag");
            tagDescription.setText(tag.getName());
            tagColour.setValue(new Color(tag.getColour().get(0),tag.getColour().get(1),
                    tag.getColour().get(2), 1.0));
            tagTextColour.setValue(new Color(tag.getColour().get(3),tag.getColour().get(4),
                    tag.getColour().get(5), 1.0));
            resultUpdate.setText("Successfully updated a tag!");
        }
    }

    /**
     * This method is used when calling this controller to make sure that after refreshing the text field remains empty
     */
    public void removeText() {
        resultUpdate.setText("");
    }

    /**
     * creates a Tag
     */
    public void createTag() {
        Color color = tagColour.getValue();
        Color textColor = tagTextColour.getValue();
        if(!tagDescription.getText().equals("")) {
            if(tag.equals(new Tag())) {
                Tag newTag = new Tag(tagDescription.getText(), currentBoard.getId(), new ArrayList<Double>(){{
                    add(color.getRed());
                    add(color.getGreen());
                    add(color.getBlue());
                    add(textColor.getRed());
                    add(textColor.getGreen());
                    add(textColor.getBlue());
                }});
                server.send("/app/tags", newTag, session);
            } else {
                Tag newTag = new Tag(tag.getId(), tagDescription.getText(), currentBoard.getId(),
                        tag.getCards(), new ArrayList<Double>(){{
                    add(color.getRed());
                    add(color.getGreen());
                    add(color.getBlue());
                    add(textColor.getRed());
                    add(textColor.getGreen());
                    add(textColor.getBlue());
                }});
                tag = newTag;
                server.send("/app/tagsUpdate", newTag, session);
            }
            refresh();
        } else {
            resultUpdate.setText("please enter any text for the tag description");
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
    public void initialize(Board board, Tag tag) {
        currentBoard = board;
        this.tag = tag;
    }

    /**
     * Used to go back to the main board view
     */
    public void goBack() {
        mainCtrl.showBoard(currentBoard);
    }
}
