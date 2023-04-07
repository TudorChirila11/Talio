package client.scenes;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class KeyboardShortcutCtrl extends Thread {

    private MainCtrl mainCtrl;

    private Stage primaryStage;

    /**
     * Constructor for the KeyboardShortcutCtrl
     *
     * @param mainCtrl     - the main controller of the application
     * @param primaryStage - the primary stage of the application
     */
    public KeyboardShortcutCtrl(MainCtrl mainCtrl, Stage primaryStage) {
        this.mainCtrl = mainCtrl;
        this.primaryStage = primaryStage;
    }


    /**
     * Run method for the thread used to check for keyboard shortcuts
     * Enables a listener that is set throughout the entire board
     */
    @Override
    public void run() {
        // creat listener for the primary scene when question mark is pressed+
        while (true) {
            Scene prev = primaryStage.getScene();
            // Only if the shift and slash was pressed then when it is realease we go
            // back to the previous scene
            primaryStage.getScene().setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
                    Platform.runLater(() -> {
                        mainCtrl.showKeyboardShortcutPage();
                    });
                }
            });
        }
    }
}
