package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import commons.Collection;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.*;
import java.util.List;

public class CardInformationCtrl implements Initializable {

    @FXML
    public Button tagAdder;

    @FXML
    public MenuButton tagChooserAdd;

    @FXML
    public MenuButton tagChooserDelete;

    @FXML
    public Button tagDeleter;

    private List<Tag> tagList;

    private List<Tag> totalTagList;

    private Tag currentTagAdd;

    private Tag currentTagDelete;

    enum State {
        EDIT, CREATE, INACTIVE
    }

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board currentBoard;

    private ArrayList<HBox> subtaskHBoxes;



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
    private Button addSubtaskButton;

    @FXML
    private TextField subtaskName;

    private List<Long> data;

    @FXML
    private Text title;

    private StompSession session;

    /**
     * sets the CardInformationCtrl's state field
     *
     * @param value the new State value of this class's state field
     */
    public void setState(State value) {
        this.state = value;
    }

    private Collection collectionCurrent;

    /**
     * Card Information Ctrl Constructor
     *
     * @param server   serverUtils ref
     * @param mainCtrl mainCtrl controller ref
     */
    @Inject
    public CardInformationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

    }


    /**
     * Initializes this controller
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagAdder.setOnAction(event -> {
            if (currentTagAdd != null && !tagList.contains(currentTagAdd)) {
                tagList.add(currentTagAdd);
                tagChooserAdd.setText("choose a tag");
                currentTagAdd = null;
                refresh();
            }
        });

        tagDeleter.setOnAction(event -> {
            if (currentTagDelete != null && tagList.contains(currentTagDelete)) {
                tagList.remove(currentTagDelete);
                tagChooserDelete.setText("choose a tag");
                currentTagDelete = null;
                refresh();
            }
        });

        subtaskHBoxes = new ArrayList<>();
        data = Collections.synchronizedList(new ArrayList<>());
        buildAddSubtask();
        setupCollectionMenu();
        card = new Card();
        collectionCurrent = null;
        //refresh();
    }

    /**
     * method to register for long polling updates
     */
    public void registerForUpdates()
    {
        server.registerForUpdates(l ->{
            data.add(l);
            if(state == State.EDIT){
                Platform.runLater(() -> checkDeleted());
            }
        });
    }

    /**
     * Checks if a card has been deleted with long polling
     */
    private void checkDeleted() {
        showError("This card has been deleted by another user!");
        if(data.contains(card.getId())){
            goBack();
        }
    }

    /**
     * stops the polling
     */
    public void stop() {
        server.stop();
    }


    /**
     * A method for starting to listen to a server once the connection has been established
     *
     * @param session the session that is connected to a server that the client is connected to
     */
    public void subscriber(StompSession session) {
        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(this::refresh), session);
        this.session = session;
    }

    /**
     * receive a card object from external sources, to populate this scene's fields with its data
     *
     * @param card - a card object
     */
    public void setCard(Card card) {
        this.card = card;
        if (card.getCollectionId() != null)
            this.collectionCurrent = server.getCollectionById(card.getCollectionId());
    }

    /**
     * sets up the MenuButton for choosing a Collection for the current Card
     */

    private void setupCollectionMenu() {
        collectionMenu.getItems().clear();

        if (currentBoard != null) {
            for (Collection c : server.getCollectionsFromBoard(currentBoard)) {

                MenuItem i = new MenuItem(c.getName());
                i.setOnAction(event -> {
                    collectionMenu.setText(i.getText());
                    collectionCurrent = c;
                });
                collectionMenu.getItems().add(i);
            }
        }
    }

    /**
     * creates the 'Add subtask' option
     *
     * @return a Hbox containing the 'Add subtask' option
     */
    private void buildAddSubtask() {
        addSubtaskButton.setOnAction(event -> {
            if (subtaskName.getText().equals("")) {
                showError("Subtask name cannot be empty");
            } else {
                Subtask subtask = new Subtask();
                subtask.setIndex((long) subtaskHBoxes.size());
                subtask.setName(subtaskName.getText());
                subtask.setCardId(card.getId());
                subtask.setFinished(false);
                card.getSubtasks().add(subtask);

                HBox hb = new HBox(10);
                TextField tf = new TextField();
                tf.setBackground(Background.EMPTY);
                Button deleteButton = new Button("x");
                deleteButton.getStyleClass().add("deleteSubTaskButton");
                Button up = new Button("^");
                Button down = new Button("v");
                CheckBox cb = new CheckBox();

                setUpSubTaskControls(subtask, tf, hb, up, down, cb, deleteButton);
                hb.getChildren().addAll(tf, cb, up, down, deleteButton);

                subtaskHBoxes.add(hb);
                subtaskName.clear();
                loadSubtasksPane();
            }

        });
    }

    /**
     * Creates the subtask controls
     *
     * @param subtask      the subtask to add the controls to
     * @param tf           the TextField to add the subtask name to
     * @param hb           the HBox to add the controls to
     * @param up           the up button
     * @param down         the down button
     * @param cb           the checkbox
     * @param deleteButton the delete button
     */
    private void setUpSubTaskControls(Subtask subtask, TextField tf, HBox hb, Button up, Button down, CheckBox cb, Button deleteButton) {
        tf.setPrefWidth(80);
        tf.setText(subtask.getName());
        renameSubtask(subtask, tf);
        cb.setOnAction(event1 -> {
            if (cb.isSelected()) {
                subtask.setFinished(true);
                tf.setDisable(true);
                tf.setOpacity(0.5);
            } else {
                subtask.setFinished(false);
                tf.setDisable(false);
                tf.setOpacity(1);
            }
            populateSubtasksScreen(card.getSubtasks());
            loadSubtasksPane();
        });
        up.getStyleClass().add("arrows");
        up.setOnAction(event13 -> {
            int index = subtaskHBoxes.indexOf(hb);
            if (index > 0) {
                Subtask swap = card.getSubtasks().get(index - 1);
                subtaskHBoxes.remove(hb);
                subtaskHBoxes.add(index - 1, hb);
                subtask.setIndex((long) index - 1);
                swap.setIndex((long) index);
                populateSubtasksScreen(card.getSubtasks());
                loadSubtasksPane();
            }
        });
        down.setOnAction(event12 -> {
            int index = subtaskHBoxes.indexOf(hb);
            if (index < subtaskHBoxes.size() - 1) {
                Subtask swap = card.getSubtasks().get(index + 1);
                swap.setIndex((long) index);
                subtaskHBoxes.remove(hb);
                subtaskHBoxes.add(index + 1, hb);
                subtask.setIndex((long) index + 1);
                populateSubtasksScreen(card.getSubtasks());
                loadSubtasksPane();
            }
        });
        down.getStyleClass().add("arrows");
        deleteButton.setOnAction(event -> {
            card.getSubtasks().remove(subtask);
            populateSubtasksScreen(card.getSubtasks());
            loadSubtasksPane();
           // toDelete.add(subtask);
        });
    }

    /**
     * Renames the subtask
     *
     * @param subtask the subtask to rename
     * @param tf      the TextField to rename the subtask to
     */
    private void renameSubtask(Subtask subtask, TextField tf) {
        tf.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TextInputDialog dialog = new TextInputDialog(tf.getText());
                dialog.setTitle("Edit Subtask");
                dialog.setHeaderText("Edit Subtask");
                dialog.setContentText("Please enter the new name of the subtask:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    subtask.setName(name);
                    tf.setText(name);
                });
                populateSubtasksScreen(card.getSubtasks());
                loadSubtasksPane();
            }
        });
    }

    /**
     * Deletes the current card.
     */
    public void deleteCard() {
        try {
            server.send("/app/cardsDelete", card.getId(), session);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Card Deleted!");
        alert.showAndWait();
        goBack();
    }


    /**
     * stores these subtasks into the database
     * @param c - the id we want to store the list of subtasks in
     */
    private void saveSubtasksCardId(Card c) {
        List<Subtask> subtasks = c.getSubtasks();
        for(Subtask s : subtasks)
            if(s.getCardId() == null)
            {
                s.setCardId(c.getId());
                Subtask newS = server.updateSubtask(s.getId(), s);
                server.send("app/subtasks", newS, session);
            }
    }

    /**
     * Method to return to the board
     */
    public void goBack() {
        clearFields();
        state = State.INACTIVE;
        mainCtrl.showBoard(currentBoard);
    }


    /**
     * Refresh method
     */
    public void refresh() {
        if(state == State.INACTIVE || state == null)
            return;
        if (currentBoard != null) {
            setupTags();
        }
        if (collectionCurrent == null)
            collectionMenu.setText("Select...");
        else collectionMenu.setText(collectionCurrent.getName());

        if (card.getSubtasks() == null || card.getId() == null)
            card.setSubtasks(new ArrayList<>());
        loadSubtasksPane();
    }

    /**
     * populates the subtask screen with a list of subtasks
     * @param subtaskList - the subtask list we want to populate the screen with
     */
    public void populateSubtasksScreen(List<Subtask> subtaskList) {
        subtaskHBoxes = new ArrayList<>();
        subtaskList.sort(Comparator.comparing(Subtask::getIndex));
     //   System.out.println(subtaskList);
        for (Subtask s : subtaskList) {
            HBox hb = new HBox(10);
            TextField tf = new TextField();
            tf.setBackground(Background.EMPTY);
            Button deleteButton = new Button("x");
            deleteButton.getStyleClass().add("deleteSubTaskButton");
            Button up = new Button("^");
            Button down = new Button("v");
            CheckBox cb = new CheckBox();
            cb.setSelected(s.getFinished());

            setUpSubTaskControls(s, tf, hb, up, down, cb, deleteButton);
            hb.getChildren().addAll(tf, cb, up, down, deleteButton);
            subtaskHBoxes.add(hb);
        }
    }
    /**
     * Function to reload only the subtasks pane visually, not the whole card information
     */
    public void loadSubtasksPane() {
        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.getChildren().addAll(subtaskHBoxes);
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
        if(state == State.CREATE) {
            card.setCollectionId(collectionCurrent.getId());
            card.setIndex((long) collectionCurrent.getCards().size());
            Card c = server.addCard(card);
            saveSubtasksCardId(c);
            server.send("/app/cards", c, session);
            updateTags(c);

        } else{
            deleteSubtasksOfCard(card.getId());
            Card c = server.updateCard(card.getId(), card);
            saveSubtasksCardId(c);
            server.send("/app/cards", c, session);
            try {
                Collection newCol = collectionCurrent;
                Long index = card.getIndex();
                Long newIndex = (long) collectionCurrent.getCards().size();
                if(oldCol!= null && (long) newCol.getId()!= (long) oldCol.getId() && state == State.EDIT) {
                    Card d = server.changeCardIndex(oldCol, index, newCol, newIndex);
                    oldCol = server.getCollectionById(oldCol.getId());
                    newCol = server.getCollectionById(newCol.getId());
                    server.send("/app/collections", oldCol, session);
                    server.send("/app/collections", newCol, session);
                    server.send("/app/cards", d, session);
                }
            } catch (WebApplicationException e) {
                showError(e.getMessage());
            }
            updateTags(c);
        }
        clearFields(); ///security measure
        state = State.INACTIVE;
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
        subtaskHBoxes.clear();
        subtaskName.clear();
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
     * @param cardId - the id of the card we want to edit
     */
    public void setEditMode(Long cardId) {
        title.setText("Edit card");
        setCard(getCardById(cardId));
        cardName.setText(card.getTitle());
        cardDescription.setText(card.getDescription());
        collectionCurrent = server.getCollectionById(card.getCollectionId());
        setState(CardInformationCtrl.State.EDIT);
        Board board = server.getBoardOfCard(cardId);
        setBoard(board);
        card.setSubtasks(server.getSubtasksOfCard(card.getId()));
        populateSubtasksScreen(card.getSubtasks());
        setupCollectionMenu();
        setTag();
        refresh();

    }

    /**
     * configures this controller to enter in 'Create Card' Mode
     * @param board - the id of the board we are currently in
     */
    public void setCreateMode(Board board) {
        collectionMenu.setText("Select...");
        collectionCurrent = null;
        title.setText("Add card");
        setState(CardInformationCtrl.State.CREATE);
        setCard(new Card());
        setBoard(board);
        setupCollectionMenu();
        setTag();
        refresh();
    }

    private void setTag() {
        if (currentBoard != null) {
            if (card.equals(new Card())) {
                tagList = new ArrayList<>();
            } else {
                tagList = server.getTagsByCard(card);
            }
            totalTagList = server.getTags(currentBoard.getId());
            setupTags();
        }
    }

    /**
     * deletes subtasks of card id
     *
     * @param id - the id of the card we want to delete the subtasks of
     */
    public void deleteSubtasksOfCard(Long id) {
        List<Subtask> subtasks = server.getSubtasksOfCard(id);
        for(Subtask s : subtasks)
        {
            server.send("/app/subtasksDelete", s.getId(), session);
        }
    }

    /**
     * This method will set up the tag menu, so that the user can add and delete cards from the card.
     */
    public void setupTags() {

        tagChooserAdd.getItems().clear();

        //Adding all the tags that the card doesn't have
        for (Tag tag : totalTagList) {
            if (!tagList.contains(tag)) {
                MenuItem menuItem = new MenuItem(tag.getName());
                menuItem.setOnAction(event -> {
                    tagChooserAdd.setText(tag.getName());
                    currentTagAdd = tag;
                });
                tagChooserAdd.getItems().add(menuItem);
            }
        }

        tagChooserDelete.getItems().clear();

        //Adding all the tags that the card has
        for (Tag tag : tagList) {
            MenuItem menuItem = new MenuItem(tag.getName());
            menuItem.setOnAction(event -> {
                tagChooserDelete.setText(tag.getName());
                currentTagDelete = tag;
            });
            tagChooserDelete.getItems().add(menuItem);
        }
    }

    /**
     * This method is used to add all the relevant card ids to all the tags
     * @param c the card that'll be added or removed from all the appropriate tags
     */
    public void updateTags(Card c) {
        // this just adds the card to all the tags that the card added
        for (Tag tag : tagList) {
            if (!tag.getCards().contains(c.getId())) {
                System.out.println(c.getId() + "hello");
                tag.getCards().add(c.getId());
                server.send("/app/tagsUpdate", tag, session);
            }
        }

        // this just deletes the card from all the tags that the card removed
        for (Tag tag : totalTagList) {
            if (tag.getCards().contains(c.getId()) && !tagList.contains(tag)) {
                System.out.println(c.getId() + "there");
                tag.getCards().remove(c.getId());
                server.send("/app/tagsUpdate", tag, session);
            }
        }
    }
}