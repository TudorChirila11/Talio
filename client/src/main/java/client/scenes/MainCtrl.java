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

    /**
     * Initializes the mainCtrl method with all the active controllers
     * @param primaryStage primary stage (active)
     * @param overview overview of quotes
     * @param add add of quote
     * @param board board scene
     * @param cardInfo cardInfo scene
     * @param collection collection scene
     * @param welcomePage welcomePage scene
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add, Pair<BoardCtrl, Parent> board, Pair<CardInformationCtrl, Parent> cardInfo,
                           Pair<CollectionOverviewCtrl, Parent> collection, Pair<WelcomePageCtrl, Parent> welcomePage) {
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
     * Displays the collection Scene
     * and enables the controller
     */
    public void showWelcomePage(){
        primaryStage.setTitle("Welcome page Overview: Welcome page");
        primaryStage.setScene(welcomePage);
    }
}