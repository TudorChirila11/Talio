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
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardInformationCtrl implements Initializable {




    enum State{
        EDIT, CREATE
    }

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ArrayList<HBox> subtasks;

    private State state;

    private Card card;

    @FXML
    private TextField cardName;

    @FXML
    private TextArea cardDescription;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private MenuButton collectionMenu;

    @FXML
    private Text title;

    /**
     * sets the CardInformationCtrl's state field
     * @param value the new State value of this class's state field
     */
    public void setState(State value)
    {
        this.state = value;
    }

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
        subtasks.add(buildAddSubtask());
        setupCollectionMenu();
        card = new Card();
        refresh();
        ///dummy part
    }

    /**
     * receive a card object from external sources, to populate this scene's fields with its data
     * @param card - a card object
     */
    public void setCard(Card card)
    {
        this.card = card;
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
        if(state == State.EDIT)
            title.setText("Edit card");
        else
            title.setText("Add card");
        cardName.setText(card.getTitle());
        cardDescription.setText(card.getDescription());
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
        card.setTitle(cardName.getText());
        card.setDescription(cardDescription.getText());

        try {
            server.addCard(card);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields(); ///security measure
        mainCtrl.showBoard();
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

    /**
     * gets a card from the database and assigns it to this scene's specific card
     * @return the specific found card
     */
    public Card getCardByName(String name) {
        ArrayList<Card> response = (ArrayList<Card>) server.getCardByName(name);
        if(response.size() != 1)
            throw new Error("Something very strange happened. I found " + response.size() +" cards with the name "+ name);
        this.card = response.get(0);
        return response.get(0);
    }
}
