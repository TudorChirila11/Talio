package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomePageCtrl implements Initializable {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    public TextField userIP;
    public Button enterButton;
    public Text errorConnection;

    /**
     * Constructor for the CollectionOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public WelcomePageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * The method will be used to test whether the ip entered is valid or not.
     * @param host a string that represents the website that will be checked.
     * @param port an integer that is the default port 8080 that'll be tested.
     * @param timeout the time in milliseconds that'll be the maximum timeout for the connection attempt.
     * @return boolean value that shows whether the website is accessible or not.
     */
    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    /**
     * makes sure that the user is connected to the right server
     * @param ip the ip that the user has entered
     */
    public void changeIP(String ip) throws InterruptedException {
        if (Objects.equals(ip, "")) {
            if (pingHost("localhost", 8080, 1000)) {
                server.changeIP("localhost");
                server.createStompSession("localhost");
                mainCtrl.showBoardOverview();
            } else {
                errorConnection.setText("The localhost server has not yet been started");
            }
        } else {
            if (pingHost(ip, 8080, 1000)) {
                server.changeIP(ip);
                server.createStompSession(ip);
                mainCtrl.showBoardOverview();
            } else {
                errorConnection.setText("The entered IP address is invalid");
            }
        }
    }

    /**
     * Initialization method fo the labels within the collection
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

        enterButton.setOnAction(event -> {
            try {
                changeIP(userIP.getText());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * This method is only used by the MainCtrl in order to give the server all the controllers
     * that it needs in order to use auto-synchronization
     * @return the only instance of server on the client
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Generates a key for the admin
     * @return the key for the admin
     */
    public String generateKey() {
        return server.getAdminKey();
    }
}