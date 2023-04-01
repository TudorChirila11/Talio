package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLogInCtrl implements Initializable {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private static boolean isAdmin;

    @FXML
    private TextField adminKey;


    /**
     * Constructor for the AdminLogInCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AdminLogInCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        isAdmin = false;
    }

    public void makeAdmin() {
        if (adminKey.getText().equals(mainCtrl.getAdmin())) {
            isAdmin = true;
            show("Welcome admin!");
        }
        mainCtrl.showBoardOverview();
    }


    public static void show(String message) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Update!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean getAdmin() {
        return isAdmin;
    }

    public void showBoardOverviewPage() {
        mainCtrl.showBoardOverview();
    }

    public ServerUtils getServer() {
        return server;
    }
}
