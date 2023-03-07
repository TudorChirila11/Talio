package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

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

    public void goBack()
    {
        mainCtrl.showBoard();
        //TODO Change to showBoard()
    }


    public void refresh()
    {
        //int baseTextX = 265, baseY = 44, baseButtonX = 427, offsetY = 72-44;

    }

    //TODO Save card to database
}
