package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class KeyboardShortcutFCtrl implements Initializable {

    @FXML
    private ScrollPane shortcutsScrollPane;

    @FXML
    private VBox shortcutsVBox;

    @FXML
    private Button backButton;


    private final ServerUtils server;

    private final MainCtrl mainCtrl;


    /**
     * Constructor for the CollectionOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public KeyboardShortcutFCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        shortcutsVBox = new VBox();
        addShortcut("Keyboard Shortcuts", 20);
        addShortcut("? - Open the Keyboard Shortcuts Menu", 14);
        addEmptyLine();
        addShortcut("Up - Move the highlight up", 14);
        addEmptyLine();
        addShortcut("Down - Move the highlight down", 14);
        addEmptyLine();
        addShortcut("Left - Move the highlight left", 14);
        addEmptyLine();
        addShortcut("Right - Move the highlight right", 14);
        addEmptyLine();
        addShortcut("Shift + Up/Down - Move highlighted task up or down", 14);
        addEmptyLine();
        addShortcut("E - Edit the highlighted task's title", 14);
        addEmptyLine();
        addShortcut("Del/ Backspace - Delete highlighted task", 14);

    }

    /**
     * Helper method for initialize()
     * @param text - the text which describes the given shortcut
     * @param fontSize - the font size used
     */
    private void addShortcut(String text, int fontSize) {
        Text shortcutText = new Text(text);
        shortcutText.setFont(new javafx.scene.text.Font(fontSize));
        shortcutsVBox.getChildren().add(shortcutText);
    }

    /**
     * Helper method for initialize()
     * Adds an empty line
     */
    private void addEmptyLine() {
        addShortcut("", 1);
    }



    /**
     * Shows the board overview
     */
    public void showBoardOverview() {
        mainCtrl.showBoardOverview();
    }

}
