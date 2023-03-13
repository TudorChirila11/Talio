package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class CardInformationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Pane emptyPane;
    private ArrayList<TextField> subtasks;
    private ArrayList<Button> subtasksButtons;

    private Scene cardInformation;
    @FXML
    private TextField cardName;

    @FXML
    private TextArea cardDescription;

    @FXML
    private AnchorPane subtaskPane;

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
        TextField tf = new TextField("Add subtask...");
        Button button = new Button("Add");
        subtasks.add(tf);
        subtasksButtons.add(button);
    }

    public void addCard()
    {
        int n = subtasks.size();
        subtasks.add(subtasks.get(n-1));
        subtasksButtons.add(subtasksButtons.get(n-1));
        subtasksButtons.get(n-1).setText("Rmv");
        refresh();
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
        System.out.println(subtasks.size());
        subtaskPane.getChildren().clear();
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
    }

    //TODO Save card to database
}
