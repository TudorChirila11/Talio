package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ColorManagementCtrl implements Initializable {

    private ServerUtils server;

    private MainCtrl mainCtrl;

    private Board currentBoard;

    @Inject
    public ColorManagementCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This method passes the current board to this controller
     * @param board the board that the tag overview is related to
     */
    public void initialize(Board board) {
        currentBoard = board;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showCurrentBoard() {
        mainCtrl.showBoard(currentBoard);
    }

}
