package client.scenes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;

public class TagInOverviewCtrl {


    public Label tagText;
    public HBox mainTagBody;

    /**
     * The method is used to set the text of the tag
     * @param tagText the text that will replace the current text
     */
    public void setTagText(String tagText) {
        this.tagText.setText(tagText);
    }

    /**
     * The method is used to set the colour of the tag
     * @param colour the rbp values that the tag's colour will be set to
     */
    public void setColor(List<Double> colour){
        mainTagBody.setStyle("-fx-background-color: " +
                new Color(colour.get(0), colour.get(1), colour.get(2), 1.0).toString().replace("0x", "#") +
                "; -fx-padding: 10 20 10 20; -fx-background-radius: 25");
    }
}
