package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.util.ArrayList;

public class CardInformationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    private ArrayList<TextField> subtasks;
    private ArrayList<Button> subtaksButtons;

    private Scene cardInformation;
    @FXML
    private TextField cardName;

    @FXML
    private TextArea cardDescription;


    /**
     * Card Information Ctrl Constructor
     * @param server serverUtils ref
     * @param mainCtrl mainCtrl controller ref
     */
    @Inject
    public CardInformationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        subtasks = new ArrayList<>();
        subtaksButtons = new ArrayList<>();
        ///TODO continue work on making subtasks appear automatically

        /*TextField tf = new TextField("Add subtask...");
        tf.setLayoutX(265);
        tf.setLayoutY(44);
        Button button = new Button("Add");
        button.setLayoutX(427);
        button.setLayoutY(44);*/
    }

    /**
     * Method to return to the board
     */
    public void goBack()
    {
        mainCtrl.showBoard();
    }


    /**
     * Refresh method
     */
    public void refresh()
    {
        //int baseTextX = 265, baseY = 44, baseButtonX = 427, offsetY = 72-44;

    }

    /**
     * Method to add Card to referencing Collection and
     * saving to database.
     */
    public void addCard(){
        try {
            server.addCard(getCard());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showBoard();
    }

    /**
     * Retrieves the values stored in the text field,areas...
     * @return A card object.
     */
    public Card getCard() {
        // null collection for now
        return new Card(cardName.getText(), cardDescription.getText());
    }

    /**
     * Clears text fields and data
     */
    public void clearFields(){
        cardName.clear();
        cardDescription.clear();
        subtasks.clear();
    }

}
