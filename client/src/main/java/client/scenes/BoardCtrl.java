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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private Button lockButton;

    @FXML
    private ScrollPane collectionsContainer;

    private boolean isLocked;

    private boolean isAccessible;

    private boolean isAdmin;


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
     * Removes the lock of a board.
     */
    private void removeLock() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove lock");
        dialog.setHeaderText("Remove lock");
        dialog.setContentText("Please enter the password:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(currentBoard.getPassword())) {
                currentBoard.setLocked(false);
                currentBoard.setPassword(null);
                server.send("/app/boards", currentBoard, session);
                isLocked = false;
                addCollectionButton.setDisable(false);
                addCardButton.setDisable(false);
            }
        }
    }

    /**
     * Checks a file containing board id, password
     * to see if the board id and password match the current board
     *
     * @return true if the board id and password match the current board
     */
    private boolean passwordCheck() {
        File file = new File("password.txt");
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String boardId = line.split("-PASSWORD-")[0];
                    String password = line.split("-PASSWORD-")[1];
                    if (boardId.equals(currentBoard.getId().toString()) && password.equals(currentBoard.getPassword())) {
                        return true;
                    }
                }
                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
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
     * Unlocks a board
     */
    private void unlockBoard() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Password");
            dialog.setHeaderText("Enter the password for the board");
            dialog.setContentText("Password:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (!result.get().equals(currentBoard.getPassword())) {
                    showAlert("Wrong password");
                    return;
                }
            }
            // Writes board id and password to a file
            File file = new File("password.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                FileWriter writer = new FileWriter(file, true);
                writer.write(currentBoard.getId().toString() + "-PASSWORD-" + currentBoard.getPassword() + "\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isLocked = false;
            addCollectionButton.setDisable(false);
            addCardButton.setDisable(false);
            refresh(currentBoard);
        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
    }

    /**
     * Locks a board
     */
    private void lockBoard() {
        try {
            if (currentBoard.getPassword() == null || currentBoard.getPassword().equals("")) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Password");
                dialog.setHeaderText("Enter a password for the board");
                dialog.setContentText("Password:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    currentBoard.setPassword(result.get());
                }
            } else {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Password");
                dialog.setHeaderText("Enter the password for the board");
                dialog.setContentText("Password:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    if (!result.get().equals(currentBoard.getPassword())) {
                        showAlert("Wrong password");
                        return;
                    }
                }
            }
            currentBoard.setLocked(true);
            server.send("/app/boards", currentBoard, session);
        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
    }

    /**
     * Sets the state of board
     *
     * @param board the current Board
     */
    public void refresh(Board board) {
        currentBoard = board;
        if (currentBoard != null) {
            lockSetup();
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
                if(isLocked && !isAccessible) {
                    setupDragAndDrop(collection);
                }
                // Creating a vertical stacked box with the label -> collection -> simple add task add button
                Button simpleAddTaskButton = new Button("+");

                if (isLocked && !isAccessible) {
                    simpleAddTaskButton.setDisable(true);
                }

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
     * Set up lock button
     */
    private void lockSetup() {
        isLocked = currentBoard.isLocked();
        lockButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)){
                removeLock();
            }else{
                if (isLocked) {
                    System.out.println("Lock button clicked");
                    unlockBoard();
                } else {
                    System.out.println("Lock button clicked");
                    lockBoard();
                }
            }
        });
        if (isLocked && !passwordCheck()) {
            lockButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/lock.png")))));
            addCollectionButton.setDisable(true);
            addCardButton.setDisable(true);
        }else{
            lockButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/unlock.png")))));
            addCollectionButton.setDisable(false);
            addCardButton.setDisable(false);
        }
        isAccessible = passwordCheck();
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
            if (sourceNode != null) {
                ListView<Card> sourceList = (ListView<Card>) sourceNode;
                int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();

                Collection oldCollection = mapper.get(sourceList);
                Collection newCollection = mapper.get(listView);
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
                    if(isLocked && !isAccessible){
                        mainCtrl.viewCard(cell.getItem().getId());
                    }else{
                        mainCtrl.editCard(cell.getItem().getId());
                    }
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
            if (listView.getItems().size() >= 4) {
                configOnDragOver(cell);
                cell.setOnDragDropped(event -> configDropped(event, listView, server.getCard(cell.getItem().getId()).getIndex(), om));
            }
            return cell;
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
        if (isLocked && !isAccessible) {
            delete.setDisable(true);
            label.setDisable(true);
        }
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
                } else showAlert("Please enter a title for the card");
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
     * sets admin
     * @param admin
     */

    public void setAdmin(boolean admin) {
    }
}
