package client.scenes;

import client.fxml.CardCell;
import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.*;
import commons.Collection;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    public AnchorPane boardPane;

    @FXML
    private Button addCollectionButton;

    @FXML
    private Button addCardButton;

    @FXML
    private Button overviewBack;
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
        if (isAdmin) {
            currentBoard.setLocked(false);
            currentBoard.setPassword(null);
            server.send("/app/boards", currentBoard, session);
            currentBoard = server.getBoardById(currentBoard.getId());
            addCollectionButton.setDisable(false);
            addCardButton.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lock removed");
            alert.setHeaderText("Lock removed");
            alert.setContentText("The lock has been removed");
            alert.showAndWait();
            refresh(currentBoard);
        } else {
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
                    currentBoard = server.getBoardById(currentBoard.getId());
                    addCollectionButton.setDisable(false);
                    addCardButton.setDisable(false);
                    refresh(currentBoard);
                }
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
        currentBoard = server.getBoardById(currentBoard.getId());
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
            if (!result.isPresent()) {
                return;
            }
            if (!result.get().equals(currentBoard.getPassword())) {
                showAlert("Wrong password");
                return;
            }
            writeNewPasswordToFile();
            addCollectionButton.setDisable(false);
            addCardButton.setDisable(false);
            refresh(currentBoard);
        } catch (WebApplicationException e) {
            showAlert(e.toString());
        }
    }

    /**
     * Writes the new password to a file
     */
    private void writeNewPasswordToFile() {
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
    }

    /**
     * Locks a board
     */
    private void lockBoard() {
        try {
            if (currentBoard.getPassword() == null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Password");
                dialog.setHeaderText("Enter a password for the board");
                dialog.setContentText("Password:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().equals("")) {
                    currentBoard.setPassword(result.get());
                } else {
                    return;
                }
            } else {
                // Changes the password of the board
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change Password");
                dialog.setHeaderText("Enter the new password for the board");
                dialog.setContentText("Password:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().equals(currentBoard.getPassword()) && !result.get().equals("")) {
                    currentBoard.setPassword(result.get());
                    writeNewPasswordToFile();
                } else {
                    return;
                }
            }
            currentBoard.setLocked(true);
            server.send("/app/boards", currentBoard, session);
            currentBoard = server.getBoardById(currentBoard.getId());
            refresh(currentBoard);
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
            List<Double> colour = currentBoard.getColor();
            if (colour != null && colour.size() != 0) {
                boardPane.setStyle("-fx-background-color: " +
                        new Color(colour.get(3), colour.get(4), colour.get(5), 1.0).toString().replace("0x", "#") +
                        ";");
                String fontColor = "-fx-text-fill: " + new Color(colour.get(0), colour.get(1), colour.get(2), 1.0).toString().replace("0x", "#");
                setBoardFontColor(fontColor);
            } else setBoardFontColor("");

            List<Double> color = currentBoard.getCollectionColor();
            String styleBG = "";
            String styleFont = "";
            if (color != null && color.size() == 6) {
                styleBG = "-fx-background-color: " + new Color(color.get(3), color.get(4), color.get(5), 1.0).toString().replace("0x", "#") + ";";
                styleFont = "-fx-text-fill: " + new Color(color.get(0), color.get(1), color.get(2), 1.0).toString().replace("0x", "#") + ";";
            }
            boardLabel.setText(board.getName());
            boardLabel.setPrefWidth(300);
            HBox.setHgrow(boardLabel, Priority.ALWAYS);
            AnchorPane.setLeftAnchor(boardLabel, 0.0);
            AnchorPane.setRightAnchor(boardLabel, 0.0);
            boardLabel.setAlignment(javafx.geometry.Pos.CENTER);
            List<Collection> taskCollections = server.getCollectionsFromBoard(currentBoard);
            // Create a horizontal box to hold the task lists
            HBox taskListsBox = new HBox(30);
            taskListsBox.setPrefSize(225 * taskCollections.size(), 275);
            mapper = new HashMap<>();
            for (Collection current : taskCollections) {
                String collectionName = current.getName();
                ObservableList<Card> list = FXCollections.observableList(server.getCardsForCollection(current));
                Label collectionLabel = new Label(collectionName);
                collectionLabel.getStyleClass().add("collectionLabel");
                ListView<Card> collection = new ListView<>(list);
                collection.getStyleClass().add("collection");
                collection.setCellFactory(new CardCellFactory(mainCtrl, server, session, isAdmin, currentBoard, isAdmin));
                collection.setPrefSize(225, 275);

                //maps this listview to its associate Collection
                mapper.put(collection, current);
                if (isAdmin) {
                    setupDragAndDrop(collection);
                } else if (!isLocked || isAccessible) {
                    setupDragAndDrop(collection);
                }
                // Creating a vertical stacked box with the label -> collection -> simple add task add button
                Button simpleAddTaskButton = new Button("+");

                if ((isLocked && !isAccessible && !isAdmin)) simpleAddTaskButton.setDisable(true);

                VBox collectionVBox = new VBox(10);
                collectionVBox.getChildren().addAll(collectionLabel, collection, simpleAddTaskButton);

                // Adding this to Hbox which contains each collection object + controls.
                taskListsBox.getChildren().add(collectionVBox);
                addTaskListControls(collectionLabel, collectionName, current, simpleAddTaskButton);

                collectionLabel.setStyle(styleFont);
                collection.setStyle(styleBG);
            }

            // Finally updating all the values in the pane with the current HBox
            collectionsContainer.setContent(taskListsBox);
        }
    }


    /**
     * Sets the board font color
     *
     * @param fontColor the new font color
     */
    public void setBoardFontColor(String fontColor) {
        tagButton.setStyle(fontColor);
        tagOverview.setStyle(fontColor);
        boardLabel.setStyle(fontColor);
        addCardButton.setStyle(fontColor);
        overviewBack.setStyle(fontColor);
    }

    /**
     * Set up lock button
     */
    private void lockSetup() {
        try {
            currentBoard = server.getBoardById(currentBoard.getId());
            isLocked = currentBoard.isLocked();
            isAccessible = passwordCheck();
            lockButton.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.SECONDARY) && (isAccessible || isAdmin || !isLocked)) {
                    removeLock();
                } else {
                    if (isLocked && !isAccessible) {
                        unlockBoard();
                    } else {
                        lockBoard();
                    }
                }
            });
            if (!isAdmin) {
                if (isLocked && !isAccessible) {
                    lockButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/lock.png")))));
                    addCollectionButton.setDisable(true);
                    addCardButton.setDisable(true);
                } else {
                    lockButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/unlock.png")))));
                    addCollectionButton.setDisable(false);
                    addCardButton.setDisable(false);
                }
            } else {
                lockButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/unlock.png")))));
                addCollectionButton.setDisable(false);
                addCardButton.setDisable(false);
            }
        } catch (BadRequestException e) {
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
            if (sourceNode != null) {
                ListView<Card> sourceList = (ListView<Card>) sourceNode;
                int sourceIndex = sourceList.getSelectionModel().getSelectedIndex();

                Collection oldCollection = mapper.get(sourceList);
                Collection newCollection = mapper.get(listView);
                if (oldCollection == newCollection)
                    newIndex = Math.min(newIndex, listView.getItems().size() - 1);
                long oldIndex = sourceIndex;
                Card d = server.changeCardIndex(oldCollection, oldIndex, newCollection, newIndex);
                server.send("/app/collections", server.getCollectionById(oldCollection.getId()), session);
                server.send("/app/collections", server.getCollectionById(newCollection.getId()), session);
                server.send("/app/cards", d, session);
                currentBoard = server.getBoardById(currentBoard.getId());
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
            CardCell cell = new CardCell(mainCtrl, server, session, isAdmin, currentBoard, isAccessible);
            cell.onMouseClickedProperty().set(event -> {
                if (event.getClickCount() == 2) {
                    if (cell.getItem() != null) {
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
     *
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
                alert.setHeaderText("Change the title for the card:");
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
            // if a user presses the right key the selected item will the next listview's cell at the same index
            if (event.getCode() == KeyCode.RIGHT) {
                listView.getSelectionModel().clearSelection();
                selectFocus(listView, 1);
                // Consume the event so that it is not processed further
                event.consume();
            }

            // if a user presses the left key the selected item will the previous listview's cell at the same index
            if (event.getCode() == KeyCode.LEFT) {
                listView.getSelectionModel().clearSelection();
                selectFocus(listView, -1);
                // Consume the event so that it is not processed further
                event.consume();
            }

            createTagShortcut(listView, event);
            createPresetShortcut(listView, event);
        });
    }

    /**
     * This method creates a shortcut for choosing a preset
     *
     * @param listView the list view of the card
     * @param event    the key event
     */

    private void createPresetShortcut(ListView<Card> listView, KeyEvent event) {
        if (event.getCode() == KeyCode.C) {
            var card = listView.getSelectionModel().getSelectedItem();
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Select the preset you want to apply to the card:");
            var presets = server.getPresets(currentBoard.getId());
            // The user can now choose one preset out of the presets to apply to the card
            var comboBox = new ComboBox<ColorPreset>();
            comboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(ColorPreset item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText("Preset: " + item.getId());
                        Color background = new Color(item.getColor().get(0), item.getColor().get(1), item.getColor().get(2), 1.0);
                        Color font = new Color(item.getColor().get(3), item.getColor().get(4), item.getColor().get(5), 1.0);
                        setStyle("-fx-background-color: " + background.toString().replace("0x", "#") + "; -fx-text-fill: " + font.toString().replace("0x", "#") + ";");
                    }
                }
            });
            comboBox.setItems(FXCollections.observableArrayList(presets));
            comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                Color background = new Color(newValue.getColor().get(0), newValue.getColor().get(1), newValue.getColor().get(2), 1.0);
                Color font = new Color(newValue.getColor().get(3), newValue.getColor().get(4), newValue.getColor().get(5), 1.0);
                comboBox.setStyle("-fx-background-color: " + background.toString().replace("0x", "#") + "; -fx-text-fill: " + font.toString().replace("0x", "#") + ";");
            });
            alert.getDialogPane().setContent(comboBox);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                var preset = comboBox.getSelectionModel().getSelectedItem();
                card.setColorPreset(preset);
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
    }

    /**
     * This method selects the next or previous cell in the list view
     *
     * @param listView the list view of the card
     * @param i        the index of the cell
     */
    private void selectFocus(ListView<Card> listView, int i) {
        VBox v = (VBox) listView.getParent();
        int indexOfList = v.getParent().getChildrenUnmodifiable().indexOf(v);
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();

        ObservableList<Node> children = v.getParent().getChildrenUnmodifiable();
        // Find the adjacent ListView
        ListView adjacentListView = null;
        if (indexOfList >= 0 && indexOfList < children.size() - 1) {
            if (i == -1 && indexOfList == 0) {
                return;
            }
            Node vbox = children.get(indexOfList + i);
            if (vbox instanceof VBox) {
                ObservableList<Node> vboxChildren = ((VBox) vbox).getChildrenUnmodifiable();
                if (vboxChildren.size() > 1) {
                    Node listViewNode = vboxChildren.get(1);
                    if (listViewNode instanceof ListView) {
                        adjacentListView = (ListView) listViewNode;
                    }
                }
            }
        }

        // Select the item at the same index in the adjacent ListView
        if (adjacentListView != null) {
            adjacentListView.requestFocus();
            adjacentListView.getSelectionModel().select(selectedIndex);
        }
    }

    /**
     * This method creates a shortcut for the tag creation
     *
     * @param listView the list view of the card
     * @param event    the event that is triggered
     */
    private void createTagShortcut(ListView<Card> listView, KeyEvent event) {
        if (event.getCode() == KeyCode.T) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Select the tags you want to add to the card:");
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
            if (checkBoxes.isEmpty()) {
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
     *
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
                        if (c1 == null)
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
                        if (c1 == null)
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
                    currentBoard = server.getBoardById(currentBoard.getId());
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
        if (isLocked && !isAccessible && !isAdmin) {
            delete.setDisable(true);
            label.setDisable(true);
        }
        delete.getStyleClass().add("delete_button");
        delete.setOnAction(event -> {
            try {
                server.send("/app/collectionsDelete", collection, session);
                currentBoard = server.getBoardById(currentBoard.getId());
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
                        currentBoard = server.getBoardById(currentBoard.getId());
                    }
                }
            }
        });
        addSimpleAddTaskButton(collection, simpleAddTaskButton);
    }

    /**
     * Adds a button to add a card to a collection
     *
     * @param collection          the collection
     * @param simpleAddTaskButton the button to add a cards
     */
    private void addSimpleAddTaskButton(Collection collection, Button simpleAddTaskButton) {
        simpleAddTaskButton.getStyleClass().add("simpleAddTaskButton");
        simpleAddTaskButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Card");
            alert.setHeaderText("Add a new card to " + collection.getName());
            alert.initModality(Modality.APPLICATION_MODAL);
            TextField input = new TextField();
            input.setPromptText("Card Title");
            alert.getDialogPane().setContent(input);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!input.getText().equals("")) {
                    Card newCard = new Card(input.getText(), "", collection, (long) (server.getCardsForCollection(collection).size()), server.getDefaultPresets(currentBoard.getId()));
                    newCard.setColorPreset(null);
                    server.send("/app/cards", newCard, session);
                    currentBoard = server.getBoardById(currentBoard.getId());
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
        pos = Math.max(0, pos);
        return pos;
    }

    /**
     * sets admin
     *
     * @param admin
     */

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Switches to the color management scene
     */
    public void showColorManagement() {
        mainCtrl.showColorManagement(currentBoard);
    }

    /**
     * BoardPane getter
     *
     * @return the BoardPane
     */
    public AnchorPane getBoardPane() {
        return boardPane;
    }
}
