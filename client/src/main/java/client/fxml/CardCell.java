package client.fxml;

import client.Main;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;

public class CardCell extends ListCell<Card> {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button removeButton;

    /**
     * Constructor for the Custom Task Cell of type Card
     */
    public CardCell() {
        loadFXML();

        removeButton.setOnAction(event -> getListView().getItems().remove(getItem()));

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
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Overriding the defined update Item for Custom Card Cell
     * @param card The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if(empty || card == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            titleLabel.setText(card.getTitle());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setText(card.getDescription());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}