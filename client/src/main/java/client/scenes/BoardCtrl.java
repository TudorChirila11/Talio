package client.scenes;

import client.fxml.CardCell;
import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Card;
import commons.Collection;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.net.URL;
import java.util.*;

public class BoardCtrl implements Initializable {
    private final ServerUtils server;

    @FXML
    private Button addCollectionButton;

    private Board currentBoard;

    @FXML
    private Label boardLabel;

    @FXML
    private ScrollPane collectionsContainer;

    @FXML
    private ComboBox<String> boardChoiceBox;

    HashMap<ListView<Card>, Collection> mapper;



    private final MainCtrl mainCtrl;

    //TODO fix checkstyle code refactoing, make a bit more readable...

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
        collectionsContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        collectionsContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Sets up the content of the Scroll Pane
        refresh(currentBoard);
        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(() -> refresh(currentBoard)));
    }

    /**
     * Resets all stuff on the board.
     */
    public void resetBoard(){
        try {
            server.send("/app/collectionsDeleteAll", currentBoard);

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try {
            server.send("/app/cardsDeleteAll", new Card());

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        System.out.println("We did it, we deleted everything!");
    }

    /**
     * Goes back to boardOverview
     */
    public void boardOverview(){
        mainCtrl.showBoardOverview();
    }


    /**
     * Sets the state of board
     * @param board the current Board
     */
    public void refresh(Board board) {
        currentBoard = board;
        if(currentBoard != null) {
            boardLabel.setText(board.getName());
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
                collection.setCellFactory(new CardCellFactory(mainCtrl, server));
                collection.setPrefSize(225, 275);

                System.out.println(current.getName() + " " + collection.getItems());
                //maps this listview to its associate Collection
                mapper.put(collection, current);

                // Set up drag and drop for the individual collections...
                setupDragAndDrop(collection);

                // Create the button that allows a user to add to a collection
                Button addButton = new Button();
                addButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/add.png")))));
                // Custom css
                addButton.getStyleClass().add("addButton");
                addButton.setOnAction(event -> addCard());

                // Creating a vertical stacked box with the label -> collection -> addButton
                VBox collectionVBox = new VBox(10);
                collectionVBox.getChildren().addAll(collectionLabel, collection, addButton);

                // Adding this to Hbox which contains each collection object + controls.
                taskListsBox.getChildren().add(collectionVBox);

                // Adding the relevant collectionLabel controls
                addTaskListControls(collectionLabel, collectionName, current);
            }

            // Finally updating all the values in the pane with the current HBox
            collectionsContainer.setContent(taskListsBox);
        }
    }

    /**
     * sets up what happens in case of drop
     * @param event - the drag event
     * @param listView - the listView in which this operation happens
     * @param newIndex - the new index this cell will be on
     * @param om - object mapper to efficiently handle card data
     */
    private void configDropped(DragEvent event, ListView<Card> listView, long newIndex, ObjectMapper om)
    {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        System.out.println(dragboard.getString());
        if (dragboard.hasString()) {
            Card card = null;
            try {
                card = om.readValue(dragboard.getString(), Card.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //listView.getItems().add(card);
            //success = true;
            // Find the source ListView by traversing up the scene graph
            Node sourceNode = (Node) event.getGestureSource();
            while (sourceNode != null && !(sourceNode instanceof ListView)) {
                sourceNode = sourceNode.getParent();
            }
            //TODO Fix the warning here...
            if (sourceNode != null) {
                ListView<Card> sourceList = (ListView<Card>) sourceNode;
                int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();
                //    Card sourceCard = sourceList.getItems().get(sourceIndex);
                //  sourceList.getItems().remove(sourceCard);

                Collection oldCollection = mapper.get(sourceList);
                Collection newCollection = mapper.get(listView);
                long oldIndex = sourceIndex;
                //int currentIndex = getIndex(listView, event.getY());
                server.changeCardIndex(oldCollection, oldIndex, newCollection, newIndex);
                refresh(currentBoard);
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }


    /**
     * configures what happens on drag over, during the drag-and-drop process
     * @param handler - object you drag over on - can be a ListView<Card> or a CardCell
     */
    public void configOnDragOver(Node handler)
    {
        handler.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof CardCell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets up the drag and Drop for all listviews stored
     * @param listView list view from the scroll view.
     */
    private void setupDragAndDrop(ListView<Card> listView) {
        ObjectMapper om = new ObjectMapper();
        if(listView.getItems().size()<4) {
            configOnDragOver(listView);
            listView.setOnDragDropped( event -> configDropped(event, listView, getIndex(listView, event.getY()), om));
        }
        listView.setCellFactory(param -> {
            CardCell cell = new CardCell(mainCtrl, server);
            cell.setOnDragDetected(event -> {
                if (cell.getItem() == null) {return;}
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
            if(listView.getItems().size()>=4) {
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
                try {
                    server.send("/app/collections", randomC);
                } catch (WebApplicationException e) {
                    e.printStackTrace();
                    e.getCause();
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Controller for Label interactions.
     * @param label the label of the collection
     * @param listName collection / list of cards name
     * @param collection the collection
     */
    private void addTaskListControls(Label label, String listName, Collection collection) {
        // Creates a button that has a delete function respective to the source collection
        Button delete = new Button("X");
        delete.setStyle("-fx-font-size: 10px; -fx-background-color: #FF0000; -fx-text-fill: white;");
        delete.setOnAction(event -> {
            try {
                server.send("/app/collectionsDelete", collection);

            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
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
                        collection.setName(newName);
                        server.send("/app/collections", collection);
                    }
                }
            }
        });
    }

    /**
     * returns this card's future index inside listview lv
     * @param lv - current listview
     * @param y - y position
     * @return this card's new index
     */
    public int getIndex(ListView<Card> lv, double y)
    {
        int sz = lv.getItems().size();
        if(sz == 0)
            return 0;
        int pos = 0;
        double cardSize = 100, error = 0;
        pos = (int) Math.min(y/(cardSize + error), sz);
        System.out.println(y + " position " + pos);
        return pos;
    }
}
