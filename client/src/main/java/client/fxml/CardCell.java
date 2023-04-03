package client.fxml;

import client.Main;
import client.scenes.MainCtrl;

import client.utils.ServerUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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

    private VBox vBox;

    private Long id;

    private StompSession session;

    /**
     * Constructor for the Custom Task Cell of type Card
     *
     * @param mainCtrl - reference for main controller
     * @param server   reference for server
     * @param session  current Stompsession for websockets
     */
    public CardCell(MainCtrl mainCtrl, ServerUtils server, StompSession session) {
        super();
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.session = session;
        loadFXML();
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    server.send("/app/cardsDelete", id, session);

                } catch (WebApplicationException e) {

                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
                getListView().getItems().remove(getItem());
            }
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
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);


            if (server.getBoardOfCard(id).isLocked() && !passwordCheck()) {
                removeButton.setDisable(true);
                editButton.setOnAction(event -> {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText("This board is locked, you can't edit cards");
                    alert.showAndWait();
                });
            } else {
                System.out.println("id is null");
            }
        }
    }
    /**
     * Checks if the password of the board is correct
     *
     * @return true if the password is correct
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
                    if (boardId.equals(server.getBoardOfCard(id).getId().toString()) && password.equals(server.getBoardOfCard(id).getPassword())) {
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


}