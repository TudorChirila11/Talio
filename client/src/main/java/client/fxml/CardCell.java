package client.fxml;

import client.Main;
import client.scenes.MainCtrl;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.ColorPreset;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.IOException;
import java.util.List;

public class CardCell extends ListCell<Card> {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    @FXML
    private Label doneSubtasks;

    @FXML
    private HBox tagInCardContainer;

    private List<Tag> tagList;


    @FXML
    private VBox mainVBox;

    private Long id;

    private StompSession session;

    private boolean passwordCheck;

    private Board currentBoard;

    private boolean isAdmin;

    /**
     * Constructor for the Custom Task Cell of type Card
     *
     * @param mainCtrl      - reference for main controller
     * @param server        reference for server
     * @param session       current Stompsession for websockets
     * @param isAdmin       boolean to check if the user is an admins
     * @param currentBoard  current board
     * @param passwordCheck boolean to check if the board is password protected
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server, StompSession session, boolean isAdmin, Board currentBoard, boolean passwordCheck) {
        super();
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.session = session;
        this.isAdmin = isAdmin;
        this.currentBoard = currentBoard;
        this.passwordCheck = passwordCheck;
        loadFXML();
        removeButton.setOnAction(event -> {
            try {
                server.send("/app/cardsDelete", id, session);
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            getListView().getItems().remove(getItem());
        });
        editButton.setOnAction(event -> {
            mainCtrl.editCard(id);
        });
    }

    /**
     * FXML Loader for the Card Cell
     */
    private void loadFXML() {
        try {
            FXMLLoader load = new FXMLLoader(Main.class.getResource("/client/scenes/CardCell.fxml"));
            load.setController(this);
            load.setRoot(this);
            load.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Overriding the defined update Item for Custom Card Cell
     *
     * @param card  The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *              is empty, then it does not represent any domain data, but is a cell
     *              being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            titleLabel.setText(card.getTitle());
            id = card.getId();
            descriptionLabel.setWrapText(true);
            descriptionLabel.setText(card.getDescription());
            doneSubtasks.setText(server.getDoneSubtasksForCard(id));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            ColorPreset curr = null;
            if (card.getColorPreset() == null)
                curr = server.getDefaultPresetForCard(card);
            else curr = card.getColorPreset();
            Color bg = new Color(curr.getColor().get(0), curr.getColor().get(1), curr.getColor().get(2), 1.0);
            Color font = new Color(curr.getColor().get(3), curr.getColor().get(4), curr.getColor().get(5), 1.0);

            if (currentBoard != null) {
                if ((currentBoard.isLocked() && !passwordCheck) && !isAdmin) {
                    removeButton.setDisable(true);
                    editButton.setOnAction(event -> {
                        var alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setContentText("This board is locked, you can't edit cards. Will enter the card in read-only mode.");
                        alert.showAndWait();
                        mainCtrl.viewCard(id);
                    });
                }
            }
            //this is for showing all the tags in the card.
            tagList = server.getTagsByCard(server.getCardById(card.getId()));
            tagInCardContainer.getChildren().removeAll(tagInCardContainer.getChildren());
            for (Tag tag : tagList) {
                Label tagLabel = new Label(tag.getName());
                List<Double> colour = tag.getColour();
                tagLabel.setStyle("-fx-background-color: " +
                        new Color(colour.get(0), colour.get(1), colour.get(2), 1.0).toString().replace("0x", "#") +
                        "; -fx-padding: 0 5 0 5; -fx-background-radius: 10;");
                tagLabel.setTextFill(new Color(colour.get(3), colour.get(4), colour.get(5), 1.0));
                tagInCardContainer.getChildren().add(tagLabel);
            }
            if (this.isSelected()) {
                mainVBox.setStyle("-fx-background-color: " + bg.toString().replace("0x", "#") + "; -fx-border-color: white;");
            } else {
                mainVBox.setStyle("-fx-background-color: " + bg.toString().replace("0x", "#"));
            }

            titleLabel.setStyle("-fx-text-fill: " + font.toString().replace("0x", "#")+";"+"-fx-font-size: 16px;-fx-padding: 3px;");
            descriptionLabel.setStyle("-fx-text-fill: " + font.toString().replace("0x", "#")+";-fx-padding: 3px;");
            doneSubtasks.setStyle("-fx-text-fill: " + font.toString().replace("0x", "#")+";");
            tagList = server.getTagsByCard(server.getCardById(card.getId()));
            tagInCardContainer.getChildren().removeAll(tagInCardContainer.getChildren());
            for (Tag tag : tagList) {
                Label tagLabel = new Label(tag.getName());
                List<Double> colour = tag.getColour();
                tagLabel.setStyle("-fx-background-color: " +
                        new Color(colour.get(0), colour.get(1), colour.get(2), 1.0).toString().replace("0x", "#") +
                        "; -fx-padding: 0 5 0 5; -fx-background-radius: 10;");
                tagLabel.setTextFill(new Color(colour.get(3), colour.get(4), colour.get(5), 1.0));
                tagInCardContainer.getChildren().add(tagLabel);
            }
        }
    }
}