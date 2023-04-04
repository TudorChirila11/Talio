package client.scenes;

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

    /**
     * ColorManagementCtrl constructor
     * @param server serverUtils ref
     * @param mainCtrl mainCtrl ref
     */
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


    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Goes back to the current board
     */
    public void showCurrentBoard() {
        mainCtrl.showBoard(currentBoard);
    }

}
