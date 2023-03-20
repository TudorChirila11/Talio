package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Collection;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardInformationCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Pane emptyPane;
    private ArrayList<HBox> subtasks;

    @FXML
    private TextField cardName;

    @FXML
    private TextArea cardDescription;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private MenuButton collectionMenu;


    /**
     * Card Information Ctrl Constructor
     * @param server serverUtils ref
     * @param mainCtrl mainCtrl controller ref
     */
    @Inject
    public CardInformationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

    }


    /**
     * Initializes this controller
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
        subtasks = new ArrayList<>();
        ///TODO see TODO for refresh()
        emptyPane = new Pane();
        subtasks.add(buildAddSubtask());
        setupCollectionMenu();
        refresh();
        ///dummy part
    }

    /**
     * sets up the MenuButton for choosing a Collection for the current Card
     */

    private void setupCollectionMenu()
    {
        Collection todo = new Collection("To-Do");
        Collection doing = new Collection("Doing");
        Collection done = new Collection("Done");
        ///
        MenuItem mi1 = new MenuItem(todo.getName());
        MenuItem mi2 = new MenuItem(doing.getName());
        MenuItem mi3 = new MenuItem(done.getName());

        mi1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                collectionMenu.setText(mi1.getText());
            }
        });

        mi2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                collectionMenu.setText(mi2.getText());
            }
        });

        mi3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                collectionMenu.setText(mi3.getText());
            }
        });
        collectionMenu.getItems().addAll(mi1, mi2, mi3);
    }

    /**
     * creates the 'Add subtask' option
     * @return a Hbox containing the 'Add subtask' option
     */
    private HBox buildAddSubtask()
    {
        TextField tf = new TextField();
        tf.setPromptText("Add subtask...");
        tf.setText("");
        Button btn = new Button("Add");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tf.getText().equals(""))
                    tf.setPromptText("I can't be empty!");
                else {
                    HBox hb = new HBox();
                    TextField tf2 = new TextField(tf.getText());
                    tf2.setEditable(false);
                    Button btn2 = new Button("Rmv");
                    hb.getChildren().addAll(tf2, btn2);

                    HBox last = subtasks.get(subtasks.size() -1);
                    subtasks.set(subtasks.size()-1, hb);
                    subtasks.add(last);
                    btn2.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            hb.getChildren().clear();
                            refresh();
                        }
                    });
                    tf.setPromptText("Add subtask...");
                    tf.setText("");
                }
                refresh();
            }
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(tf, btn);
        return hbox;
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
        ///TODO Retrieve subtasks from the database and put them inside the "subtasks" arraylist
        ///TODO Retrieve all collections from the database and put them as options inside the "Choose collection" menu

        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.getChildren().addAll(subtasks);
        scrollPane.setContent(vbox);

    }

    /**
     * Method to add Card to referencing Collection and
     * saving to database.
     */
    public void addCard(){
        if(cardName.getText().equals(""))
        {
            cardName.setPromptText("I can't be empty!");
            return;
        }

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
        subtasks.add(buildAddSubtask());
    }

}
