package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Collection;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardInformationCtrl implements Initializable {



    enum State{
        EDIT, CREATE
    }

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board currentBoard;

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

    private StompSession session;

    /**
     * sets the CardInformationCtrl's state field
     * @param value the new State value of this class's state field
     */
    public void setState(State value)
    {
        this.state = value;
    }
    private Collection collectionCurrent;

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
        collectionCurrent = null;
        refresh();
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
     * receive a card object from external sources, to populate this scene's fields with its data
     * @param card - a card object
     */
    public void setCard(Card card)
    {
        this.card = card;
        if(card.getCollectionId() != null)
            this.collectionCurrent = server.getCollectionById(card.getCollectionId());
    }
    /**
     * sets up the MenuButton for choosing a Collection for the current Card
     */

    private void setupCollectionMenu() {
        collectionMenu.getItems().clear();
        if(collectionCurrent == null)
            collectionMenu.setText("Select...");
        else collectionMenu.setText(collectionCurrent.getName());

        if (currentBoard != null) {
            for (Collection c : server.getCollectionsFromBoard(currentBoard)) {

                MenuItem i = new MenuItem(c.getName());
                i.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        collectionMenu.setText(i.getText());
                        collectionCurrent = c;
                    }
                });
                collectionMenu.getItems().add(i);
            }
        }
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
        mainCtrl.showBoard(currentBoard);
    }


    /**
     * Refresh method
     */
    public void refresh()
    {

        setupCollectionMenu();
        if(state == State.EDIT)
        {
            title.setText("Edit card");
            collectionCurrent = server.getCollectionById(card.getCollectionId());
        }
        else
        {
            title.setText("Add card");
            collectionCurrent = null;
        }
        if(collectionCurrent == null)
            collectionMenu.setText("Select...");
        else collectionMenu.setText(collectionCurrent.getName());
        cardName.setText(card.getTitle());
        cardDescription.setText(card.getDescription());

        ///TODO Retrieve subtasks from the database and put them inside the "subtasks" arraylist

        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.getChildren().addAll(subtasks);
        scrollPane.setContent(vbox);

    }

    /**
     * Method to add Card to referencing Collection and
     * saving to database.
     */
    public void addCard() {
        if (cardName.getText().equals("")) {
            showError("Card name cannot be empty!");
            return;
        }
        if (collectionCurrent == null) {
            showError("You need to select a collection for this card!");
            return;
        }
        card.setTitle(cardName.getText());
        card.setDescription(cardDescription.getText());
        Collection oldCol = null;
        if(card.getCollectionId()!=null)
            oldCol = server.getCollectionById(card.getCollectionId());
        if(state == State.CREATE)
        {
            card.setCollectionId(collectionCurrent.getId());
            card.setIndex((long) collectionCurrent.getCards().size());
            Card c = server.addCard(card);
            server.send("/app/cards", c, session);
        }
        else
        {
            Card c = server.updateCard(card.getId(), card);
            server.send("/app/cards", c, session);
            try {

                Collection newCol = collectionCurrent;
                Long index = card.getIndex();
                Long newIndex = (long) collectionCurrent.getCards().size();

                if(oldCol!= null && (long) newCol.getId()!= (long) oldCol.getId() && state == State.EDIT) {
                    Card d = server.changeCardIndex(oldCol, index, newCol, newIndex);
                    server.send("/app/collections", oldCol, session);
                    server.send("/app/collections", newCol, session);
                    server.send("/app/cards", d, session);
                }

            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
        }
        clearFields(); ///security measure
        mainCtrl.showBoard(currentBoard);
    }

    /**
     * Retrieves the values stored in the text field,areas...
     * @return A card object.
     */
    public Card getCard() {
        return new Card(cardName.getText(), cardDescription.getText(), collectionCurrent, Long.valueOf(collectionCurrent.getCards().size()));
    }

    /**
     * To delete a card
     * @param id of card
     */
    public void deleteCard(long id){
        server.deleteCard(id);
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
     * displays an alert with the given text.
     * this method exists to avoid boilerplate code
     * @param text - the message to be displayed inside the error
     */
    private void showError(String text)
    {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(text);
        a.show();
    }

    /**
     * returns a card with specified id
     * @param cardId - the id of the card we want to search
     * @return - a card object
     */
    public Card getCardById(Long cardId) {

        Card card = server.getCardById(cardId);
        return card; ///null check?
    }

    /**
     * Board getter
     * @return the board we are currently running this scene in
     */
    public Board getBoard() {
        return currentBoard;
    }

    /**
     * Board setter
     * @param board - the new value of this class's currentBoard field
     */
    public void setBoard(Board board)
    {
        this.currentBoard = board;
    }

    /**
     * configures this controller to enter in 'Edit Card' Mode
     * @param cardId - the Id of the card we want to edit
     */
    public void setEditMode(Long cardId)
    {
        setCard(getCardById(cardId));
        collectionCurrent = server.getCollectionById(card.getCollectionId());
        setState(CardInformationCtrl.State.EDIT);
        Board board = server.getBoardOfCard(cardId);
        setBoard(board);
        refresh();
    }

    /**
     * configures this controller to enter in 'Create Card' Mode
     * @param board - the id of the board we are currently in
     */
    public void setCreateMode(Board board)
    {
        setState(CardInformationCtrl.State.CREATE);
        setCard(new Card());
        setBoard(board);
        refresh();
    }
}