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

import commons.Tag;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import commons.Board;


public class MainCtrl {

    private Stage primaryStage;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private Scene cardInformation;
    private CardInformationCtrl cardInformationCtrl;

    private BoardCtrl boardCtrl;

    private Scene board;

    private WelcomePageCtrl welcomePageCtrl;

    private Scene welcomePage;

    private KeyboardShortcutFCtrl keyboardShortcutFCtrl;

    private Scene keyboardShortcut;

    private AdminLogInCtrl adminLogInCtrl;

    private Scene adminLogIn;


    private TagCreatorCtrl tagCreatorCtrl;

    private Scene tagCreator;

    private TagOverviewCtrl tagOverviewCtrl;

    private Scene tagOverview;

    private ColorManagementCtrl colorManagementCtrl;

    private Scene colorManagement;



    /**
     * Initializes the mainCtrl method with all the active controllers
     * @param primaryStage primary stage (active)
     * @param board board scene
     * @param cardInfo cardInfo scene
     * @param welcomePage welcomePage scene
     * @param keyboardShortcut keyboardShortcut scene
     * @param tagCreator tagCreator scene
     * @param tagOverview tagCreator scene
     * @param boardOverview  boardOverview scene
     * @param adminLogIn adminLogIn scene
     * @param colorManagement colorManagement scene
     */
    public void initialize(Stage primaryStage, Pair<BoardCtrl, Parent> board, Pair<CardInformationCtrl, Parent> cardInfo,
                           Pair<WelcomePageCtrl, Parent> welcomePage, Pair<KeyboardShortcutFCtrl, Parent> keyboardShortcut,
                           Pair<TagCreatorCtrl, Parent> tagCreator, Pair<TagOverviewCtrl, Parent> tagOverview,
                           Pair<BoardOverviewCtrl, Parent> boardOverview, Pair<AdminLogInCtrl, Parent> adminLogIn,
                           Pair<ColorManagementCtrl, Parent> colorManagement) {

        this.primaryStage = primaryStage;

        this.cardInformationCtrl = cardInfo.getKey();
        this.cardInformation = new Scene(cardInfo.getValue());

        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.welcomePageCtrl = welcomePage.getKey();
        this.welcomePage = new Scene(welcomePage.getValue());

        this.keyboardShortcutFCtrl = keyboardShortcut.getKey();
        this.keyboardShortcut = new Scene(keyboardShortcut.getValue());

        this.tagCreatorCtrl = tagCreator.getKey();
        this.tagCreator = new Scene(tagCreator.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.adminLogInCtrl = adminLogIn.getKey();
        this.adminLogIn = new Scene(adminLogIn.getValue());

        this.colorManagementCtrl = colorManagement.getKey();
        this.colorManagement = new Scene(colorManagement.getValue());


        adminLogInCtrl.setAdmin(false);

        welcomePageCtrl.getServer().getControllers(boardCtrl, boardOverviewCtrl, tagOverviewCtrl, cardInformationCtrl, tagCreatorCtrl, adminLogInCtrl, colorManagementCtrl);

        showWelcomePage();
        primaryStage.show();
    }

    /**
     * Displays the BoardOverviewAdmin Scene
     * @param admin the admin key boolean
     */
    public void showBoardOverviewAdmin(boolean admin) {
        boardOverviewCtrl.setAdmin(admin);
        boardCtrl.setAdmin(admin);
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(boardOverview);
    }

    /**
     * Sets admin
     * @param admin afwe
     */
    public void setAdminKey(String admin) {
        adminLogInCtrl.setAdminKey(admin);
    }

    /**
     * Displays the color management Scene
     * and enables the controller
     * @param currentBoard the current Board
     */
    public void showColorManagement(Board currentBoard) {
        primaryStage.setTitle("Color Management");
        primaryStage.setScene(colorManagement);
        colorManagementCtrl.initialize(currentBoard);
    }

    /**
     * Displays the overview Scene
     * and enables the controller
     */
    public void showBoardOverview() {
        primaryStage.setTitle("Boards: Overview");
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.refresh();
    }


    /**
     * Displays the cardInformation Scene when adding card
     * and enables the controller
     * @param currentBoard the current Board
     */
    public void showCardInformation(Board currentBoard)
    {
        cardInformationCtrl.setCreateMode(currentBoard);
        primaryStage.setTitle(("Card Information"));
        primaryStage.setScene(cardInformation);
    }


    /**
     * Displays the board Scene
     * and enables the controller
     * @param currentBoard the current Board
     */
    public void showBoard(Board currentBoard){
        primaryStage.setTitle("Board Overview: Board");
        primaryStage.setScene(board);
        boardCtrl.refresh(currentBoard);
    }

    /**
     * Displays the tag creation Scene
     * and enables the controller
     * @param currentBoard the board that the tag creation window is related to
     * @param tag the tag that is to indicate whether a tag will be created or updated
     */
    public void showTagCreation(Board currentBoard, Tag tag){
        primaryStage.setTitle("Tag Creation Window");
        primaryStage.setScene(tagCreator);
        tagCreatorCtrl.initialize(currentBoard, tag);
        tagCreatorCtrl.refresh();
        tagCreatorCtrl.removeText();
    }

    /**
     * Displays the tag creation Scene
     * and enables the controller
     * @param currentBoard the board that the tag overview window is related to
     */
    public void showTagOverview(Board currentBoard){
        primaryStage.setTitle("Tag Overview Window");
        primaryStage.setScene(tagOverview);
        tagOverviewCtrl.initialize(currentBoard);
        tagOverviewCtrl.refresh();
    }

    /**
     * Displays the Admin Log In Scene
     * and enables the controller
     */
    public void showAdminLogIn() {
        primaryStage.setTitle("Admin Log In: Admin");
        primaryStage.setScene(adminLogIn);
    }

    /**
     * edit card method
     * @param cardId - id of the card we want to edit
     */
    public void editCard(Long cardId) {
        cardInformationCtrl.setEditMode(cardId);
        primaryStage.setTitle("Edit card");
        primaryStage.setScene(cardInformation);
    }

    /**
     * Getter for the board field
     * @return Scene - the board scene
     */
    public Scene getBoard() {
        return board;
    }


    /**
     * Getter for the cardInformation field
     * @return Scene - the cardInformation scene
     */
    public Scene getCardInformation() {
        return cardInformation;
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
        welcomePageCtrl.requestFocus();
    }

    /**
     * Displays the keyboardShortcuts Scene
     * and enables the controller
     */
    public void showKeyboardShortcutPage() {
        primaryStage.setTitle("Keyboard Shortcuts Overview: Keyboard Shortcuts page");
        primaryStage.setScene(keyboardShortcut);
    }

    /**
     * Displays the cardInformation Scene when viewing card
     * and enables the controller
     * @param cardId the id of the card that is being identified
     */
    public void viewCard(Long cardId) {
        cardInformationCtrl.setViewMode(cardId);
        primaryStage.setTitle("View card");
        primaryStage.setScene(cardInformation);
    }
}

