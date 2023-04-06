package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.springframework.messaging.simp.stomp.StompSession;
import java.util.List;

public class TagInOverviewCtrl {

    private ServerUtils server;

    private Tag tag;


    public Label tagText;
    public HBox mainTagBody;
    public Button editTagButton;
    public Button deleteTagButton;

    private StompSession session;

    private MainCtrl mainCtrl;

    private Board currentBoard;

    /**
     * The method is used to set the text of the tag
     * @param tagText the text that will replace the current text
     */
    public void setTagText(String tagText) {
        this.tagText.setText(tagText);
    }

    /**
     * A method for starting to listen to a server once the connection has been established
     * @param session the session that is connected to a server that the client is connected to
     * @param server the client side server that'll be used to send data to the actual server
     * @param tag the tag that this controller represents
     * @param mainCtrl this controller will be used in case a tag needs to get updated
     * @param currentBoard the board that this tag is related to
     */
    public void subscriber(StompSession session, ServerUtils server, Tag tag, MainCtrl mainCtrl, Board currentBoard) {
        this.session = session;
        this.server = server;
        this.tag = tag;
        this.mainCtrl = mainCtrl;
        this.currentBoard = currentBoard;
    }

    /**
     * This method lets the user delete exactly one tag from the overview
     */
    public void deleteTag(){
        server.send("/app/tagsDelete", tag, session);
    }

    /**
     * this method will send the tag that is to be edited to the tagCreation controller
     */
    public void editTag(){
        mainCtrl.showTagCreation(currentBoard, tag);
    }

    /**
     * The method is used to set the colour of the tag
     * @param colour the rbp values that the tag's colour will be set to
     */
    public void setColor(List<Double> colour){
        mainTagBody.setStyle("-fx-background-color: " +
                new Color(colour.get(0), colour.get(1), colour.get(2), 1.0).toString().replace("0x", "#") +
                "; -fx-padding: 10 20 10 20; -fx-background-radius: 25;");
        tagText.setTextFill(new Color(colour.get(3), colour.get(4), colour.get(5), 1.0));
    }
}
