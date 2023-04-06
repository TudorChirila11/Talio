package client.scenes;

import client.fxml.CardCell;
import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Card;
import commons.Collection;
import commons.Tag;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.*;

public class BoardCtrl implements Initializable {
    private final ServerUtils server;

    @FXML
    public Button tagButton;
    public Button tagOverview;

    @FXML
    private Button addCollectionButton;

    @FXML
    private Button addCardButton;


    private Board currentBoard;

    @FXML
    private Label boardLabel;

    @FXML
    private ScrollPane collectionsContainer;


    HashMap<ListView<Card>, Collection> mapper;


    private final MainCtrl mainCtrl;

    private StompSession session;

    /**
     * Board Ctrl constructor
     *
     * @param server   serverUtils ref
     * @param mainCtrl mainCtrl controller ref
     */
    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Adding a card from the + button
     */
    public void addCard() {
        mainCtrl.showCardInformation(currentBoard);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collectionsContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        collectionsContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Sets up the content of the Scroll Pane

        tagButton.setOnAction(event -> mainCtrl.showTagCreation(currentBoard, new Tag()));
        tagOverview.setOnAction(event -> mainCtrl.showTagOverview(currentBoard));


        refresh(currentBoard);
    }

    /**
     * A method for starting to listen to a server once the connection has been established
     *
     * @param session the session that is connected to a server that the client is connected to
     */
    public void subscriber(StompSession session) {
        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(() -> refresh(currentBoard)), session);
        this.session = session;
    }

    /**
     * Resets all stuff on the board.
     */
    public void resetBoard() {
        try {
            server.send("/app/collectionsDeleteAll", currentBoard, session);

        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
        try {
            server.send("/app/cardsDeleteAll", new Card(), session);

        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
        try {
            server.send("/app/tagsDeleteAll", new Tag(), session);

        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
    }

    /**
     * Goes back to boardOverview
     */
    public void boardOverview() {
        mainCtrl.showBoardOverview();
    }


    /**
     * Sets the state of board
     *
     * @param board the current Board
     */
    public void refresh(Board board) {
        currentBoard = board;
        if (currentBoard != null) {
            boardLabel.setText(board.getName());
            boardLabel.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(boardLabel, 0.0);
            AnchorPane.setRightAnchor(boardLabel, 0.0);
            boardLabel.setAlignment(javafx.geometry.Pos.CENTER);
            List<Collection> taskCollections = server.getCollectionsFromBoard(currentBoard);
            // Create a horizontal box to hold the task lists
            HBox taskListsBox = new HBox(25);
            taskListsBox.setPrefSize(225 * taskCollections.size(), 275);
            mapper = new HashMap<ListView<Card>, Collection>();
            for (Collection current : taskCollections) {
                String collectionName = current.getName();
                ObservableList<Card> list = FXCollections.observableList(server.getCardsForCollection(current));
                // Create a label for the collection name
                Label collectionLabel = new Label(collectionName);
                collectionLabel.getStyleClass().add("collectionLabel");

                ListView<Card> collection = new ListView<>(list);
                collection.getStyleClass().add("collection");
                collection.setCellFactory(new CardCellFactory(mainCtrl, server, session));
                collection.setPrefSize(225, 275);

                //maps this listview to its associate Collection
                mapper.put(collection, current);

                // Set up drag and drop for the individual collections...
                setupDragAndDrop(collection);
                // Creating a vertical stacked box with the label -> collection -> simple add task add button
                Button simpleAddTaskButton = new Button("+");

                VBox collectionVBox = new VBox(10);
                collectionVBox.getChildren().addAll(collectionLabel, collection, simpleAddTaskButton);

                // Adding this to Hbox which contains each collection object + controls.
                taskListsBox.getChildren().add(collectionVBox);
                addTaskListControls(collectionLabel, collectionName, current, simpleAddTaskButton);
            }

            // Finally updating all the values in the pane with the current HBox
            collectionsContainer.setContent(taskListsBox);
        }
    }

    /**
     * Shows an alert for the user
     *
     * @param s the string to be shown
     */
    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    /**
     * sets up what happens in case of drop
     *
     * @param event    - the drag event
     * @param listView - the listView in which this operation happens
     * @param newIndex - the new index this cell will be on
     * @param om       - object mapper to efficiently handle card data
     */
    private void configDropped(DragEvent event, ListView<Card> listView, long newIndex, ObjectMapper om) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;

        if (dragboard.hasString()) {
            Card card = null;
            try {
                card = om.readValue(dragboard.getString(), Card.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // Find the source ListView by traversing up the scene graph
            Node sourceNode = (Node) event.getGestureSource();
            while (sourceNode != null && !(sourceNode instanceof ListView)) {
                sourceNode = sourceNode.getParent();
            }
            //TODO Fix the warning here...
            if (sourceNode != null) {
                ListView<Card> sourceList = (ListView<Card>) sourceNode;
                int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();

                Collection oldCollection = mapper.get(sourceList);
                Collection newCollection = mapper.get(listView);
                if (oldCollection == newCollection)
                    newIndex = Math.min(newIndex, listView.getItems().size() - 1);
                long oldIndex = sourceIndex;
                //int currentIndex = getIndex(listView, event.getY());
                Card d = server.changeCardIndex(oldCollection, oldIndex, newCollection, newIndex);
                server.send("/app/collections", server.getCollectionById(oldCollection.getId()), session);
                server.send("/app/collections", server.getCollectionById(newCollection.getId()), session);
                server.send("/app/cards", d, session);
                refresh(currentBoard);
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }


    /**
     * configures what happens on drag over, during the drag-and-drop process
     *
     * @param handler - object you drag over on - can be a ListView<Card> or a CardCell
     */
    public void configOnDragOver(Node handler) {
        handler.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof CardCell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets up the drag and Drop for all listviews stored
     *
     * @param listView list view from the scroll view.
     */
    private void setupDragAndDrop(ListView<Card> listView) {
        ObjectMapper om = new ObjectMapper();
        if (listView.getItems().size() < 4) {
            configOnDragOver(listView);
            listView.setOnDragDropped(event -> configDropped(event, listView, getIndex(listView, event.getY()), om));
        }
        listView.setCellFactory(param -> {
            CardCell cell = new CardCell(mainCtrl, server, session);
            cell.onMouseClickedProperty().set(event -> {
                if (event.getClickCount() == 2) {
                    mainCtrl.editCard(cell.getItem().getId());
                }
            });
            cell.setOnDragDetected(event -> {
                if (cell.getItem() == null) {
                    return;
                }
                Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                try {
                    content.putString(om.writeValueAsString(cell.getItem()));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                dragboard.setContent(content);
                event.consume();
            });
            selectCell(listView);
            dragAndDropShortcut(listView);
            generalShortcuts(listView);

            if (listView.getItems().size() >= 4) {
                configOnDragOver(cell);
                cell.setOnDragDropped(event -> configDropped(event, listView, server.getCard(cell.getItem().getId()).getIndex(), om));
            }
            return cell;
        });
    }

    /**
     * This method initializes the shortcuts for the list view
     * @param listView the list view of the card
     */
    private void generalShortcuts(ListView<Card> listView) {
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                mainCtrl.editCard(listView.getSelectionModel().getSelectedItem().getId());
            }
            // if the user pressed the Backspace we delete the selected card
            if (event.getCode() == KeyCode.BACK_SPACE) {
                try {
                    server.send("/app/cardsDelete", listView.getSelectionModel().getSelectedItem().getId(), session);
                } catch (WebApplicationException e) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            // if the user presses E then a text field will appear where he can edit the name of the card
            if (event.getCode() == KeyCode.E) {
                var alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Change the title for the card:");
                var textField = new TextField();
                alert.getDialogPane().setContent(textField);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    var card = listView.getSelectionModel().getSelectedItem();
                    card.setTitle(textField.getText());
                    try {
                        server.send("/app/cards", card, session);
                    } catch (WebApplicationException e) {
                        var alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.initModality(Modality.APPLICATION_MODAL);
                        alert2.setContentText(e.getMessage());
                        alert2.showAndWait();
                    }
                }
            }
            createTagShortcut(listView, event);
        });
    }

    /**
     * This method creates a shortcut for the tag creation
     * @param listView the list view of the card
     * @param event the event that is triggered
     */
    private void createTagShortcut(ListView<Card> listView, KeyEvent event) {
        if(event.getCode() == KeyCode.T){
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Select the tags you want to add to the card:");
            var card = listView.getSelectionModel().getSelectedItem();
            var tags = server.getTags(currentBoard.getId());
            var tagsOnCard = server.getTagsByCard(card);
            var tagsNotOnCard = new ArrayList<Tag>();
            for (Tag t : tags) {
                if (!tagsOnCard.contains(t)) {
                    tagsNotOnCard.add(t);
                }
            }
            var checkBoxes = new ArrayList<CheckBox>();
            for (Tag t : tagsNotOnCard) {
                var checkBox = new CheckBox(t.getName());
                checkBoxes.add(checkBox);
            }
            if(checkBoxes.isEmpty()){
                showAlert("There are no tags to add to the card.");
                return;
            }
            var gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));
            for (int i = 0; i < checkBoxes.size(); i++) {
                gridPane.add(checkBoxes.get(i), 0, i);
            }
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                for (CheckBox c : checkBoxes) {
                    if (c.isSelected()) {
                        for (Tag t : tagsNotOnCard) {
                            if (t.getName().equals(c.getText())) {
                                t.getCards().add(card.getId());
                                try {
                                    server.send("/app/tags", t, session);
                                } catch (WebApplicationException e) {
                                    var alert2 = new Alert(Alert.AlertType.ERROR);
                                    alert2.initModality(Modality.APPLICATION_MODAL);
                                    alert2.setContentText(e.getMessage());
                                    alert2.showAndWait();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This configures the drag and drop of a card (shortcut)
     * @param listView the list view of the card
     */
    private void dragAndDropShortcut(ListView<Card> listView) {
        listView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.UP) {
                var card = listView.getSelectionModel().getSelectedItem();
                int index = listView.getItems().indexOf(card);
                if (index > 0) {
                    try {
                        Collection c1 = mapper.get(listView);
                        if(c1 == null)
                            return;
                        Card d = server.changeCardIndex(c1, index, c1, index - 1);
                        server.send("/app/collections", server.getCollectionById(c1.getId()), session);
                        server.send("/app/cards", d, session);
                        refresh(currentBoard);
                    } catch (WebApplicationException e) {
                        var alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.initModality(Modality.APPLICATION_MODAL);
                        alert2.setContentText(e.getMessage());
                        alert2.showAndWait();
                    }
                    event.consume();
                }
            }
        });

        listView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.DOWN) {
                var card = listView.getSelectionModel().getSelectedItem();
                int index = listView.getItems().indexOf(card);
                if (index < listView.getItems().size() - 1) {
                    try {
                        Collection c1 = mapper.get(listView);
                        if(c1 == null)
                            return;
                        Card d = server.changeCardIndex(c1, index, c1, index + 1);
                        server.send("/app/collections", server.getCollectionById(c1.getId()), session);
                        server.send("/app/cards", d, session);
                        refresh(currentBoard);
                    } catch (WebApplicationException e) {
                        var alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.initModality(Modality.APPLICATION_MODAL);
                        alert2.setContentText(e.getMessage());
                        alert2.showAndWait();
                    }
                    event.consume();
                }
            }
        });
    }


    /**
     * This configures the highlighting of a cell
     */
    private void selectCell(ListView<Card> listView) {
        listView.setOnMouseMoved(event -> {
            var cell1 = event.getTarget();
            if (cell1 == null)
                return;
            // if the target is not a cell, we traverse up the scene graph until we find a cell
            while (cell1.getClass() != CardCell.class && cell1.getClass() != Node.class) {
                // null check is needed because the scene graph can be arbitrarily deep
                cell1 = ((Node) cell1).getParent();
                if (cell1 == null)
                    return;
            }
            listView.requestFocus();
            // cast this to a cell
            CardCell c = (CardCell) cell1;
            // Select the list of this cell to have mouse events be registerd
            c.setOnMouseEntered(event1 -> {
                listView.getSelectionModel().select(c.getItem());
                // if the user presses shift up they drag and drop the card up one position
            });
        });
    }


    /**
     * This will be changed to the actual addCollection Method
     * and Scene
     */
    public void addCollection() {
        // Add Collection
        TextInputDialog interact = new TextInputDialog("New Collection");
        interact.setHeaderText("New Collection");
        interact.setContentText("Enter name:");
        Optional<String> result = interact.showAndWait();
        // Check for valid input
        if (result.isPresent()) {
            String newName = result.get();
            if (!newName.isEmpty()) {
                Collection randomC = new Collection(newName, currentBoard);
                try {
                    server.send("/app/collections", randomC, session);
                } catch (WebApplicationException e) {
                    showAlert(e.getMessage());
                }
            }
        }
    }

    /**
     * Controller for Label interactions.
     *
     * @param label               the label of the collection
     * @param listName            collection / list of cards name
     * @param collection          the collection
     * @param simpleAddTaskButton the button to add a cards
     */
    private void addTaskListControls(Label label, String listName, Collection collection, Button simpleAddTaskButton) {
        Button delete = new Button("X");
        delete.getStyleClass().add("delete_button");
        delete.setOnAction(event -> {
            try {
                server.send("/app/collectionsDelete", collection, session);
            } catch (WebApplicationException e) {
                showAlert(e.getMessage());
            }
        });
        label.setGraphic(delete);
        label.setContentDisplay(ContentDisplay.RIGHT);
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Rename collection
                TextInputDialog interact = new TextInputDialog(listName);
                interact.setHeaderText("Rename Collection");
                interact.setContentText("Enter new name:");
                Optional<String> result = interact.showAndWait();
                if (result.isPresent()) {
                    String newName = result.get();
                    if (!newName.isEmpty()) {
                        collection.setName(newName);
                        server.send("/app/collections", collection, session);
                    }
                }
            }
        });

        simpleAddTaskButton.getStyleClass().add("simpleAddTaskButton");
        simpleAddTaskButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Card");
            alert.setHeaderText("Add a new card to " + collection.getName());
            alert.setContentText("Please enter the title of the card:");
            alert.initModality(Modality.APPLICATION_MODAL);
            TextField input = new TextField();
            input.setPromptText("Card Title");
            alert.getDialogPane().setContent(input);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!input.getText().equals("")) {
                    Card newCard = new Card(input.getText(), "", collection, (long) (server.getCardsForCollection(collection).size()));
                    server.send("/app/cards", newCard, session);
                } else {
                    showAlert("Please enter a title for the card");
                }
            }
        });
    }

    /**
     * returns this card's future index inside listview lv
     *
     * @param lv - current listview
     * @param y  - y position
     * @return this card's new index
     */
    public int getIndex(ListView<Card> lv, double y) {
        int sz = lv.getItems().size();
        if (sz == 0)
            return 0;
        int pos = 0;
        double cardSize = 100, error = 0;
        pos = (int) Math.min(y / (cardSize + error), sz);

        return pos;
    }

    /**
     * Switches to the color management scene
     */
    public void showColorManagement() {
        mainCtrl.showColorManagement(currentBoard);
    }
}
