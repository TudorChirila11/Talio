package client.fxml;

import client.Main;
import client.scenes.MainCtrl;
import commons.Card;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CardCellFactory implements Callback<ListView<Card>, ListCell<Card>> {


    private final MainCtrl mainCtrl;

    public CardCellFactory(MainCtrl mainCtrl)
    {
        super();
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
        return new CardCell(mainCtrl);
    }
}