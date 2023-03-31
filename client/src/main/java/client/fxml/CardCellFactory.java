package client.fxml;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Card;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CardCellFactory implements Callback<ListView<Card>, ListCell<Card>> {


    private final MainCtrl mainCtrl;


    private final ServerUtils server;

    /**
     * Constructor that takes the server reference
     * @param mainCtrl - main controller
     * @param server - server referecne
     */

    public CardCellFactory(MainCtrl mainCtrl, ServerUtils server) {
        super();
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Creates a new Card Cell
     * @param param The single argument upon which the returned value should be
     *      determined.
     * @return A NEW CARD cELL
     */
    @Override
    public ListCell<Card> call(ListView<Card> param) {

        return new CardCell(mainCtrl, server);
    }
}