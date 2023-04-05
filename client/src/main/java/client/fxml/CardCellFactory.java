package client.fxml;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.messaging.simp.stomp.StompSession;

public class CardCellFactory implements Callback<ListView<Card>, ListCell<Card>> {


    private final MainCtrl mainCtrl;


    private final ServerUtils server;

    private StompSession session;

    private boolean isAdmin;

    private Board currentBoard;

    private boolean passwordCheck;

    /**
     * Constructor that takes the server reference
     * @param mainCtrl - main controller
     * @param server - server reference
     * @param session - session reference for websockets
     * @param isAdmin - boolean to check if the user is an admin
     * @param currentBoard - current board
     * @param passwordCheck - boolean to check if the board is password protected
     */

    public CardCellFactory(MainCtrl mainCtrl, ServerUtils server, StompSession session, boolean isAdmin, Board currentBoard, boolean passwordCheck) {
        super();
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.session = session;
        this.isAdmin = isAdmin;
        this.currentBoard = currentBoard;
        this.passwordCheck = passwordCheck;
    }

    /**
     * Creates a new Card Cell
     * @param param The single argument upon which the returned value should be
     *      determined.
     * @return A NEW CARD cELL
     */
    @Override
    public ListCell<Card> call(ListView<Card> param) {

        return new CardCell(mainCtrl, server, session, isAdmin, currentBoard, passwordCheck);
    }
}