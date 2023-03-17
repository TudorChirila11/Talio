package client.scenes;

import client.fxml.CardCell;
import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Collection;
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

import java.net.URL;
import java.util.*;

public class BoardCtrl implements Initializable {
    private final ServerUtils server;

    @FXML
    private Button addCollectionButton;

    @FXML
    private ScrollPane collectionsContainer;

    @FXML
    private ComboBox<String> boardChoiceBox;

    // Create a map to hold each task list
    // Key = Collection Name
    // Value = Items to be added to listview
    private Map<String, ObservableList<Card>> taskCollections = new LinkedHashMap<>();

    // One board for now.
    private Board board = new Board();

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
        mainCtrl.showCardInformation();
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

        // Initialize with some dummy cards...
        ObservableList<Card> toDoCards;

        Collection todo = new Collection("To-do", board);
        todo.getCards().add(new Card("Clean my Keyboard", "Make sure my keyboard is squeaky clean!", todo));
        todo.getCards().add(new Card("Organize My Desk", "Arrange everything on my desk neatly and efficiently.", todo));
        todo.getCards().add(new Card("Water My Plants", "Give my plants the hydration they need to thrive.", todo));
        toDoCards = FXCollections.observableList(todo.getCards());

        ObservableList<Card> doingCards;
        Collection doing = new Collection("Doing", board);
        doing.getCards().add(new Card("Clean my Keyboard", "Make sure my keyboard is squeaky clean!", doing));
        doing.getCards().add(new Card("Organize My Desk", "Arrange everything on my desk neatly and efficiently.", doing));
        doing.getCards().add(new Card("Water My Plants", "Give my plants the hydration they need to thrive.", doing));
        doingCards = FXCollections.observableList(doing.getCards());

        // Initialize with some dummy cards...
        ObservableList<Card> doneCards;
        Collection done = new Collection("Doing", board);
        done.getCards().add(new Card("Clean my Keyboard", "Make sure my keyboard is squeaky clean!", done));
        done.getCards().add(new Card("Organize My Desk", "Arrange everything on my desk neatly and efficiently.", done));
        done.getCards().add(new Card("Water My Plants", "Give my plants the hydration they need to thrive.", done));
        doneCards = FXCollections.observableList(done.getCards());

        // Add to Linked HashMap (Order is remembered -> Potential later rearrangement)
        taskCollections.put("To Do", toDoCards);
        taskCollections.put("Doing", doingCards);
        taskCollections.put("Done", doneCards);

        // Sets up the content of the Scroll Pane
        refresh();

        // Dummy values for the combo box.
        ObservableList<String> boards = FXCollections.observableArrayList();
        boards.add("Board 1");
        boards.add("Board 2");
        boards.add("Board 3");
        boards.add("Board 4");
        boardChoiceBox.setItems(boards);
    }


    /**
     * Sets the state of board
     */
    private void refresh() {
        // Create a horizontal box to hold the task lists
        HBox taskListsBox = new HBox(25);
        taskListsBox.setPrefSize(225 * taskCollections.size(), 275);

        // Add each task list to the box
        for (Map.Entry<String, ObservableList<Card>> entry : taskCollections.entrySet()) {
            String collectionName = entry.getKey();
            ObservableList<Card> list = entry.getValue();

            // Create a label for the collection name
            Label collectionLabel = new Label(collectionName);
            collectionLabel.getStyleClass().add("collectionLabel");


            // Create a list view for the current (list of cards)
            ListView<Card> collection = new ListView<>(list);
            collection.getStyleClass().add("collection");
            collection.setCellFactory(new CardCellFactory());
            collection.setPrefSize(225, 275);


            // Set up drag and drop for the individual collections...
            setupDragAndDrop(collection);

            // Create the button that allows a user to add to a collection
            Button addButton = new Button();
            addButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/client/assets/add.png"))));
            // Custom css
            addButton.getStyleClass().add("addButton");
            addButton.setOnAction(event -> addCard());

            // Creating a vertical stacked box with the label -> collection -> addButton
            VBox collectionVBox = new VBox(10);
            collectionVBox.getChildren().addAll(collectionLabel, collection, addButton);

            // Adding this to Hbox which contains each collection object + controls.
            taskListsBox.getChildren().add(collectionVBox);

            // Adding the relevant collectionLabel controls
            addTaskListControls(collectionLabel, collectionName, taskListsBox);
        }

        // Finally updating all the values in the pane with the current HBox
        collectionsContainer.setContent(taskListsBox);
    }

    /**
     * Sets up the drag and Drop for all listviews stored
     * @param listView list view from the scroll view.
     */
    private void setupDragAndDrop(ListView<Card> listView) {
//        // Initializes the cell factory from the custom card cell
//        listView.setCellFactory(param -> {
//            CardCell cell = new CardCell();
//            cell.setOnDragDetected(event -> {
//                if (cell.getItem() == null) {return;}
//                Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
//                ClipboardContent content = new ClipboardContent();
//                content.putString(cell.getItem().getTitle() + "-----" +cell.getItem().getDescription() +  "-----" +
//                    cell.getItem().getCollection().getName());
//                dragboard.setContent(content);
//                event.consume();
//            });
//            cell.setOnDragOver(event -> {
//                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
//                    event.acceptTransferModes(TransferMode.MOVE);
//                }
//                event.consume();
//            });
//            cell.setOnDragDropped(event -> {
//                Dragboard dragboard = event.getDragboard();
//                boolean success = false;
//                //TODO This can be done better without direct parsing...
//                if (dragboard.hasString()) {
//                    String[] card = dragboard.getString().split("-----");
//                    // This doesn't really work
//                    Card newCard = new Card(card[0], card[1], new Collection(card[2], board));
//                    listView.getItems().add(newCard);
//                    success = true;
//
//                    // Find the source ListView by traversing up the scene graph
//                    Node sourceNode = (Node) event.getGestureSource();
//                    while (sourceNode != null && !(sourceNode instanceof ListView)) {
//                        sourceNode = sourceNode.getParent();
//                    }
//
//                    //TODO Fix the warning here...
//                    if (sourceNode instanceof ListView) {
//                        ListView<Card> sourceList = (ListView<Card>) sourceNode;
//                        int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();
//                        Card sourceCard = sourceList.getItems().get(sourceIndex);
//                        sourceList.getItems().remove(sourceCard);
//                    }
//                }
//                event.setDropCompleted(success);
//                event.consume();
//            });
//            return cell;
//        });
    }



    /**
     * This will be changed to the actual addCollection Method
     * and Scene
     */
    public void addCollection(){
        // This is like dummy data...
        ObservableList<Card> random;

        Collection randomC = new Collection("Random", board);
        randomC.getCards().add(new Card("Clean my Keyboard", "Make sure my keyboard is squeaky clean!", randomC));
        randomC.getCards().add(new Card("Organize My Desk", "Arrange everything on my desk neatly and efficiently.", randomC));
        randomC.getCards().add(new Card("Water My Plants", "Give my plants the hydration they need to thrive.", randomC));
        random = FXCollections.observableList(randomC.getCards());
        taskCollections.put(randomC.getName(), random);
        refresh();

    }

    /**
     * Controller for Label interactions.
     * @param label the label of the collection
     * @param listName collection / list of cards name
     * @param taskListsBox the parent HBOxs
     */
    private void addTaskListControls(Label label, String listName, HBox taskListsBox) {
        // Creates a button that has a delete function respective to the source collection
        // Maybe combine this with above method...
        Button delete = new Button("X");
        delete.setStyle("-fx-font-size: 10px; -fx-background-color: #FF0000; -fx-text-fill: white;");
        delete.setOnAction(event -> {
            taskCollections.remove(listName);
            // Parent ref passed
            taskListsBox.getChildren().remove(label.getParent());
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
                        taskCollections.put(newName, taskCollections.remove(listName));
                        label.setText(newName);
                    }
                }
            }
        });
    }

}
