package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Subtask;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.ArrayList;

public class CardInformationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Pane emptyPane;
    private ArrayList<TextField> subtasks;
    private ArrayList<Button> subtasksButtons;

    @FXML
    private TextField cardName;

    @FXML
    private TextArea cardDescription;

    //@FXML
   // private ListView<Subtask> subtaskPane;

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
        subtasksButtons = new ArrayList<>();
        emptyPane = new Pane();
        ///TODO continue work on making subtasks appear automatically
       /* VBox vbox = new VBox();
        TextField tf = new TextField("Add elements");
        Button btn = new Button("Add");
        vbox.getChildren().addAll(tf, btn);*/

    }

    public void removeCard(int id)
    {

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
        /*subtaskPane.getChildren().clear();
        int baseTextX = 265, baseY = 44, baseButtonX = 427, offsetY = 72-44;
        for(int i = 0 ;i < subtasks.size(); ++i)
        {
            subtasks.get(i).setLayoutX(baseTextX);
            subtasks.get(i).setLayoutY(baseY);
            subtasks.get(i).setMaxWidth(149.6);
            subtasks.get(i).setMinWidth(149.6);
            subtasks.get(i).setMinHeight(25.6);
            subtasks.get(i).setMaxHeight(25.6);

            subtasksButtons.get(i).setLayoutX(baseButtonX);
            subtasksButtons.get(i).setLayoutY(baseY);

            subtaskPane.getChildren().add(subtasks.get(i));
            subtaskPane.getChildren().add(subtasksButtons.get(i));
            baseY+=offsetY;
        }
         */
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
        return new Card(cardName.getText(), cardDescription.getText(), 67543);
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
