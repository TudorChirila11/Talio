package client.scenes;


import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class KeyboardShortcutCtrl extends Thread {

    private MainCtrl mainCtrl;

    private HashMap<String, Boolean> activeKeys = new HashMap<>();

    public KeyboardShortcutCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void run() {
        mainCtrl.getBoard().setOnKeyPressed(event -> {
            String code = event.getCode().toString();
            if (!activeKeys.containsKey(code)) {
                activeKeys.put(code, true);
            }
        });
        mainCtrl.getBoard().setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode().toString());
        });
        while (true) {
            if (removeActiveKey(KeyCode.SLASH.toString())) {
                //TODO make transition to keyboard shortcuts menu
            }
            if (removeActiveKey(KeyCode.UP.toString())) {
                //TODO make method for moving highlight up
            }
            if (removeActiveKey(KeyCode.DOWN.toString())) {
                //TODO make method for moving highlight down
            }
            if (removeActiveKey(KeyCode.LEFT.toString())) {
                //TODO make method for moving highlight left
            }
            if (removeActiveKey(KeyCode.RIGHT.toString())) {
                //TODO make method for moving highlight right
            }
            if (removeActiveKey(KeyCode.UP.toString()) && removeActiveKey(KeyCode.SHIFT.toString())) {
                //TODO make method for moving highlighted task/card up
            }
            if (removeActiveKey(KeyCode.DOWN.toString()) && removeActiveKey(KeyCode.SHIFT.toString())) {
                //TODO make method for moving highlighted task/card down
            }
            if (removeActiveKey(KeyCode.E.toString())) {
                //TODO make method for editing cards
            }
            if (removeActiveKey(KeyCode.DELETE.toString()) || removeActiveKey(KeyCode.BACK_SPACE.toString())) {
                //TODO make method to delete highlighted task
            }
            if (removeActiveKey(KeyCode.ENTER.toString())) {
                //TODO make a screen where I can see the card's details
            }
            if (removeActiveKey(KeyCode.ESCAPE.toString())) {
                //TODO close the highlighted card's details
            }
            if (removeActiveKey(KeyCode.T.toString())) {
                //TODO open popup for adding tags
            }
            if (removeActiveKey(KeyCode.C.toString())) {
                //TODO open popup for customization
            }
        }
    }

    private boolean removeActiveKey(String code) {
        Boolean isActive = activeKeys.get(code);

        if (isActive != null && isActive) {
            activeKeys.put(code, false);
            return true;
        } else {
            return false;
        }
    }
}
