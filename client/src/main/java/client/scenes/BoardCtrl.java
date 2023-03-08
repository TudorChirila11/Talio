package client.scenes;

import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {
    private final ServerUtils server;

    @FXML
    private ListView<Card> todoCollection;



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
        todoCollection.setCellFactory(new CardCellFactory());

        ObservableList<Card> sample = FXCollections.observableArrayList();
        sample.add(new Card("Clean my Keyboard", "Make sure my keyboard is squeaky clean!"));
        sample.add(new Card("Organize My Desk", "Arrange everything on my desk neatly and efficiently."));
        sample.add(new Card("Water My Plants", "Give my plants the hydration they need to thrive."));
        todoCollection.setItems(sample);
    }
}
