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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;

    private Scene add;
    private Scene cardInformation;
    private CardInformationCtrl cardInformationCtrl;

    private BoardCtrl boardCtrl;

    private Scene board;

    private CollectionOverviewCtrl collectionOverviewCtrl;

    private Scene collection;

    private WelcomePageCtrl welcomePageCtrl;

    private Scene welcomePage;

    private KeyboardShortcutFCtrl keyboardShortcutFCtrl;

    private Scene keyboardShortcut;

    private TagCreatorCtrl tagCreatorCtrl;

    private Scene tagCreator;

    private TagOverviewCtrl tagOverviewCtrl;

    private Scene tagOverview;


    /**
     * Initializes the mainCtrl method with all the active controllers
     * @param primaryStage primary stage (active)
     * @param overview overview of quotes
     * @param add add of quote
     * @param board board scene
     * @param cardInfo cardInfo scene
     * @param collection collection scene
     * @param welcomePage welcomePage scene
     * @param keyboardShortcut keyboardShortcut scene
     * @param tagCreator tagCreator scene
     * @param tagOverview tagCreator scene
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add, Pair<BoardCtrl, Parent> board, Pair<CardInformationCtrl, Parent> cardInfo,
                           Pair<CollectionOverviewCtrl, Parent> collection, Pair<WelcomePageCtrl, Parent> welcomePage,
                           Pair<KeyboardShortcutFCtrl, Parent> keyboardShortcut, Pair<TagCreatorCtrl, Parent> tagCreator,
                           Pair<TagOverviewCtrl, Parent> tagOverview) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.cardInformationCtrl = cardInfo.getKey();
        this.cardInformation = new Scene(cardInfo.getValue());

        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.collectionOverviewCtrl = collection.getKey();
        this.collection = new Scene(collection.getValue());

        this.welcomePageCtrl = welcomePage.getKey();
        this.welcomePage = new Scene(welcomePage.getValue());

        this.keyboardShortcutFCtrl = keyboardShortcut.getKey();
        this.keyboardShortcut = new Scene(keyboardShortcut.getValue());

        this.tagCreatorCtrl = tagCreator.getKey();
        this.tagCreator = new Scene(tagCreator.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        showWelcomePage();
        primaryStage.show();
    }

    /**
     * Displays the overview Scene
     * and enables the controller
     */
    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    /**
     * Displays the quote add Scene
     * and enables the controller
     */
    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    /**
     * Displays the cardInformation Scene
     * and enables the controller
     */
    public void showCardInformation()
    {
        primaryStage.setTitle(("Card Information"));
        primaryStage.setScene(cardInformation);
        cardInformationCtrl.refresh();
    }

    /**
     * Displays the board Scene
     * and enables the controller
     */
    public void showBoard(){
        primaryStage.setTitle("Board Overview: Board");
        primaryStage.setScene(board);
        boardCtrl.refresh();
    }

    /**
     * Displays the tag creation Scene
     * and enables the controller
     */
    public void showTagCreation(){
        primaryStage.setTitle("Tag Creation Window");
        primaryStage.setScene(tagCreator);
    }

    /**
     * Displays the tag creation Scene
     * and enables the controller
     */
    public void showTagOverview(){
        primaryStage.setTitle("Tag Overview Window");
        primaryStage.setScene(tagOverview);
    }

    /**
     * Displays the collection Scene
     * and enables the controller
     */
    public void showCollection(){
        primaryStage.setTitle("Collection Overview: Collection");
        primaryStage.setScene(collection);
    }

    /**
     * Getter for the board field
     * @return Scene - the board scene
     */
    public Scene getBoard() {
        return board;
    }

    /**
     * Getter for the overview field
     * @return Scene - the overview scene
     */
    public Scene getOverview() {
        return overview;
    }

    /**
     * Getter for the add field
     * @return Scene - the add scene
     */
    public Scene getAdd() {
        return add;
    }

    /**
     * Getter for the cardInformation field
     * @return Scene - the cardInformation scene
     */
    public Scene getCardInformation() {
        return cardInformation;
    }

    /**
     * Getter for the collection field
     * @return Scene - the collection scene
     */
    public Scene getCollection() {
        return collection;
    }

    /**
     * Getter for the keyboardShortcut field
     * @return Scene - the keyboardShortcut scene
     */
    public Scene getKeyboardShortcut() {
        return keyboardShortcut;
    }

    /**
     * Getter for the welcomePage field
     * @return Scene - the welcomePage scene
     */
    public Scene getWelcomePage() {
        return welcomePage;
    }

    /**
     * Getter for the primaryStage field
     * @return Stage - the scene that is currently being displayed
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Displays the collection Scene
     * and enables the controller
     */
    public void showWelcomePage(){
        primaryStage.setTitle("Welcome page Overview: Welcome page");
        primaryStage.setScene(welcomePage);
    }

    /**
     * Displays the keyboardShortcuts Scene
     * and enables the controller
     */
    public void showKeyboardShortcutPage() {
        primaryStage.setTitle("Keyboard Shortcuts Overview: Keyboard Shortcuts page");
        primaryStage.setScene(keyboardShortcut);
    }
}