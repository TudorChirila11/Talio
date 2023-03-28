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

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {


    @FXML
    private ScrollPane boardContainer;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    /**
     * Constructor for the BoardOverview Ctrl
     * @param server serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(() -> refresh()));
    }

    /**
     * Refreshes the board Overview
     */
    public void refresh(){
        List<Board> allBoards = server.getBoards();
        // Vbox to contain all boards.
        VBox boardsBox = new VBox(25);
        boardsBox.setPrefSize(600,225 * allBoards.size());
        for(Board b: allBoards){
            HBox boardContent = new HBox(25);
            Label boardLabel = new Label(b.getName());
            renameBoard(boardLabel, b);

            Button copyKey = new Button();
            copyKey.setPrefSize(30,40);
            copyKey.getStyleClass().add("imageButton");
            copyKey.setOnAction(event -> showWelcomePage());
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/key.png"))));
            imageView.setPreserveRatio(true);
            imageView.setPickOnBounds(true);
            imageView.setFitHeight(40);
            imageView.setFitWidth(30);
            copyKey.setGraphic(imageView);

            Button join = new Button("Open Board");
            join.setPrefSize(100,25);
            join.getStyleClass().add("defaultButton");
            join.setOnAction(event -> mainCtrl.showBoard(b));

            Button delete = new Button("X");
            delete.getStyleClass().add("deleteButton");
            delete.setOnAction(event -> {
                try {
                    server.send("/app/boardsDelete", b);
                } catch (WebApplicationException e) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });


            boardContent.getChildren().addAll(boardLabel, copyKey, join, delete);
            boardsBox.getChildren().add(boardContent);
        }
        boardContainer.setContent(boardsBox);
    }

    /**
     * Creates method for renaming board label
     * @param boardLabel board's label
     * @param board the board
     */
    private void renameBoard(Label boardLabel, Board board) {
        boardLabel.getStyleClass().add("collectionLabel");
        boardLabel.setPrefSize(350,25);
        boardLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Rename collection
                TextInputDialog interact = new TextInputDialog(board.getName());
                interact.setHeaderText("Rename Board");
                interact.setContentText("Enter new name:");
                Optional<String> result = interact.showAndWait();
                if (result.isPresent()) {
                    String newName = result.get();
                    if (!newName.isEmpty()) {
                        board.setName(newName);
                        server.send("/app/boards", board);

                    }
                }
            }
        });
    }

    /**
     * Adds a board.
     */
    public void addBoard(){
        // Add Collection
        TextInputDialog interact = new TextInputDialog("New Board");
        interact.setHeaderText("New Board");
        interact.setContentText("Enter name:");
        Optional<String> result = interact.showAndWait();
        // Check for valid input
        if (result.isPresent()) {
            String newName = result.get();
            if (!newName.isEmpty()) {
                Board boardN = new Board(newName);
                try {
                    server.send("/app/boards", boardN);

                } catch (WebApplicationException e) {

                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
            }
        }
        refresh();
    }

    /**
     * Switch back to welcome page
     */
    public void showWelcomePage(){
        mainCtrl.showWelcomePage();
    }
}