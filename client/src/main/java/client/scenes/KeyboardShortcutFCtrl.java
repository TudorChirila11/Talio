package client.scenes;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void addShortcut(String text, int fontSize) {
        Text shortcutText = new Text(text);
        shortcutText.setFont(new javafx.scene.text.Font(fontSize));
        shortcutsVBox.getChildren().add(shortcutText);
    }

    private void addEmptyLine() {
        addShortcut("", 1);
    }



}
