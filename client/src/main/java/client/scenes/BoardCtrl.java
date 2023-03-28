package client.scenes;

import client.fxml.CardCell;
import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Collection;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.net.URL;
import java.util.*;

public class BoardCtrl implements Initializable {


    private final ServerUtils server;
    private Board currentBoard;
    @FXML
    private Button addCollectionButton;
    @FXML
    private ScrollPane collectionsContainer;

    @FXML
    private MenuButton boardMenu;

    private final MainCtrl mainCtrl;


    /**
     * Board Ctrl constructor
     * @param server serverUtils ref
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
    public void addCard(){
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
        server.getBoard();

        collectionsContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        collectionsContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Sets up the content of the Scroll Pane
        refresh(currentBoard);
    }

    /**
     * Resets all stuff on the board.
     */
    public void resetBoard(){
        server.resetState();
        Board board = new Board("Board 1");
        server.addBoard(board);
        refresh(currentBoard);
    }

    /**
     * Goes back to boardOverview
     */
    public void boardOverview(){
        mainCtrl.showBoardOverview();
    }

    /**
     * Sets the state of board
     * @param board current board
     * */
    public void refresh(Board board) {
        currentBoard = board;
        boardMenu.getItems().clear();
        for(Board b: server.getBoards()){
            MenuItem i = new MenuItem(b.getName());
            i.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    boardMenu.setText(i.getText());
                    currentBoard = b;
                    refresh(currentBoard);
                }
            });
            boardMenu.getItems().add(i);
        }
        if(currentBoard == null) currentBoard = server.getBoard();
        List<Collection> taskCollections = server.getCollectionsFromBoard(currentBoard);

        HBox taskListsBox = new HBox(25);
        taskListsBox.setPrefSize(225 * taskCollections.size(), 275);
        // Add each task list to the box
        for (Collection current: taskCollections) {
            ObservableList<Card> list = FXCollections.observableList(server.getCardsForCollection(current));
            // Create a label for the collection name
            Label collectionLabel = new Label(current.getName());
            collectionLabel.getStyleClass().add("collectionLabel");
            // Create a list view for the current (list of cards)
            ListView<Card> collection = new ListView<>(list);
            collection.getStyleClass().add("collection");
            collection.setCellFactory(new CardCellFactory(server));
            collection.setPrefSize(225, 275);
            setupDragAndDrop(collection);

            // Create the button that allows a user to add to a collection
            Button addButton = new Button();
            addButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/client/assets/add.png"))));
            addButton.getStyleClass().add("addButton");
            addButton.setOnAction(event -> addCard());

            // Creating a vertical stacked box with the label -> collection -> addButton
            VBox collectionVBox = new VBox(10);
            collectionVBox.getChildren().addAll(collectionLabel, collection, addButton);

            // Adding this to Hbox which contains each collection object + controls.
            taskListsBox.getChildren().add(collectionVBox);
            addTaskListControls(collectionLabel, current.getName(), current.getId());
        }
        // Finally updating all the values in the pane with the current HBox
        collectionsContainer.setContent(taskListsBox);
    }

    /**
     * Sets up the drag and Drop for all listviews stored
     * @param listView list view from the scroll view.
     */
    private void setupDragAndDrop(ListView<Card> listView) {
        listView.setCellFactory(param -> {
            CardCell cell = new CardCell(server);
            cell.setOnDragDetected(event -> {
                if (cell.getItem() == null) {return;}
                Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(cell.getItem().getTitle() + "-----" +cell.getItem().getDescription() +  "-----");
                dragboard.setContent(content);
                event.consume();
            });
            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });
            cell.setOnDragDropped(event -> {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;
                //TODO This can be done better without direct parsing...
                if (dragboard.hasString()) {
                    String[] card = dragboard.getString().split("-----");
                    // This doesn't really work
                    Card newCard = new Card(card[0], card[1]);
                    listView.getItems().add(newCard);
                    success = true;

                    // Find the source ListView by traversing up the scene graph
                    Node sourceNode = (Node) event.getGestureSource();
                    while (sourceNode != null && !(sourceNode instanceof ListView)) {
                        sourceNode = sourceNode.getParent();
                    }

                    //TODO Fix the warning here...
                    if (sourceNode instanceof ListView) {
                        ListView<Card> sourceList = (ListView<Card>) sourceNode;
                        int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();
                        Card sourceCard = sourceList.getItems().get(sourceIndex);
                        sourceList.getItems().remove(sourceCard);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            });
            return cell;
        });
    }



    /**
     * This will be changed to the actual addCollection Method
     * and Scene
     */
    public void addCollection(){
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
                currentBoard.addCollection(randomC);
                try {
                    server.addCollection(randomC);
                } catch (WebApplicationException e) {
                    e.printStackTrace();
                    e.getCause();
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
            }
        }
        refresh(currentBoard);
    }

    /**
     * Controller for Label interactions.
     * @param label the label of the collection
     * @param listName collection / list of cards name
     * @param id the collection id
     */
    private void addTaskListControls(Label label, String listName, long id) {
        // Creates a button that has a delete function respective to the source collection
        Button delete = new Button("X");
        delete.setStyle("-fx-font-size: 10px; -fx-background-color: #FF0000; -fx-text-fill: white;");
        delete.setOnAction(event -> {
            server.deleteCollection(id);
            refresh(currentBoard);
        });
        label.setGraphic(delete);
        label.setContentDisplay(ContentDisplay.RIGHT);

        // TODO Change this to the correct method from another team member.
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Rename collection
                TextInputDialog interact = new TextInputDialog(listName);
                interact.setHeaderText("Rename Collection");
                interact.setContentText("Enter new name:");
                Optional<String> result = interact.showAndWait();
                // Check for valid input
                if (result.isPresent()) {
                    String newName = result.get();
                    if (!newName.isEmpty()) {
                        // TODO update based on Teun's new API
                        label.setText(newName);
                    }
                }
            }
        });
    }

}
