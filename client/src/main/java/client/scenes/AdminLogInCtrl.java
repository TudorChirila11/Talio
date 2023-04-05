package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLogInCtrl implements Initializable {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private boolean isAdmin;

    private String admin;

    @FXML
    private TextField adminKey;

    private StompSession session;


    /**
     * Constructor for the AdminLogInCtrl
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AdminLogInCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * A method for starting to listen to a server once the connection has been established
     *
     * @param session the session that is connected to a server that the client is connected to
     */
    public void subscriber(StompSession session) {
        this.session = session;
    }


    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Makes the current user an admin and displays a popup to let them know
     * the operation was successful
     */
    public void makeAdmin() {
        if (adminKey.getText().equals(admin)) {
            this.isAdmin = true;
            show("Welcome admin!");
        }
        showBoardOverviewPage();
    }


    /**
     * Helper method to create a popup with a message
     *
     * @param message String that will become the displayed message
     */
    public static void show(String message) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Update!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Getter to check if a user is an admin
     *
     * @return boolean checks if the user is an admin
     */
    public boolean getAdmin() {
        return isAdmin;
    }

    /**
     * Displays the board overview Scene
     */
    public void showBoardOverviewPage() {
        mainCtrl.showBoardOverviewAdmin(isAdmin);
    }

    /**
     * Getter for the server
     *
     * @return ServerUtils - the server
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Sets the admin key
     *
     * @param admin
     */
    public void setAdminKey(String admin) {
        this.admin = admin;
    }


    /**
     * Setter for the isAdmin boolean
     *
     * @param admin - boolean for the admin
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
