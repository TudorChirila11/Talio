/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.fxml.CardCellFactory;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CollectionOverviewCtrl implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private VBox container;

    @FXML
    private Label collectionName;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    /**
     * Constructor for the CollectionOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public CollectionOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Adding a card from the + button
     */
    public void addCard(){
        mainCtrl.showCardInformation();
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

        Collection testCollection = new Collection("Test text", new Board());
        testCollection.getCards().add(new Card("Test title", "test description test description" +
                " test description", testCollection));
        testCollection.getCards().add(new Card("Test title1", "test description test description" +
                " test description test description test description beep boop", testCollection));
        testCollection.getCards().add(new Card("Test title2", "test hahahahaah", testCollection));

        ObservableList<Card> doneCards = FXCollections.observableList(testCollection.getCards());

        // Create a list view for the current (list of cards)
        ListView<Card> collection = new ListView<>(doneCards);
        collection.getStyleClass().add("collection");
        collection.setCellFactory(new CardCellFactory(server));

        container.getChildren().add(collection);

        collectionName.setText(testCollection.getName());

        addButton.setOnAction(event -> addCard());
    }
}