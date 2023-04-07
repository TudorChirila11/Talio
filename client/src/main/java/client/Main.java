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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;


import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Main Method for Client
     * @param args System args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Start method for the application's client
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        var cardInformation = FXML.load(CardInformationCtrl.class, "client", "scenes", "CardInformation.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");
        var collection = FXML.load(CollectionOverviewCtrl.class, "client", "scenes", "Collection.fxml");
        var welcomePage = FXML.load(WelcomePageCtrl.class, "client", "scenes", "WelcomePage.fxml");
        var keyboardShortcutPage = FXML.load(KeyboardShortcutFCtrl.class, "client", "scenes", "KeyboardShortcuts.fxml");
        var tagCreator = FXML.load(TagCreatorCtrl.class, "client", "scenes", "TagCreator.fxml");
        var tagOverview = FXML.load(TagOverviewCtrl.class, "client", "scenes", "TagOverview.fxml");
        var boardOverview = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var adminLogIn = FXML.load(AdminLogInCtrl.class, "client", "scenes", "AdminLogIn.fxml");
        var colorManagement = FXML.load(ColorManagementCtrl.class, "client", "scenes", "ColorManagement.fxml");

        mainCtrl.initialize(primaryStage, board, cardInformation, collection, welcomePage, keyboardShortcutPage,
                tagCreator, tagOverview, boardOverview, adminLogIn, colorManagement);

        KeyboardShortcutCtrl keyboardShortcutCtrl = new KeyboardShortcutCtrl(mainCtrl, primaryStage);
        keyboardShortcutCtrl.start();

        primaryStage.setOnCloseRequest(e -> {
            cardInformation.getKey().stop();
            keyboardShortcutCtrl.stop();
        });
    }
}