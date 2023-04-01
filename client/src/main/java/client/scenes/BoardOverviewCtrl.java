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
import client.scenes.AdminLogInCtrl;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class BoardOverviewCtrl implements Initializable {


    @FXML
    private ScrollPane boardContainer;

    @FXML
    private TextField boardKey;


    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private static String boardFilePath;

    private StompSession session;

    /**
     * Constructor for the BoardOverview Ctrl
     *
     * @param server   serverUtils ref
     * @param mainCtrl main controller ref
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialization method fo the labels within the collection
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * A method for starting to listen to a server once the connection has been established
     * @param session the session that is connected to a server that the client is connected to
     */
    public void subscriber(StompSession session) {
        server.registerForCollections("/topic/update", Object.class, c -> Platform.runLater(this::refresh), session);
        this.session = session;
        boardFilePath = "boards_"+ server.getIp() + ".txt";
    }

    /**
     * Refreshes the board Overview
     */
    public void refresh() {
        if (!AdminLogInCtrl.getAdmin()) {
            try {
                File current = new File(boardFilePath);
                Scanner scanner = new Scanner(current);
                // Vbox to contain all boards.
                VBox boardsBox = new VBox(25);
                int size = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    long boardID = Long.parseLong(line.split(" -BOOL- ")[0]);
                    System.out.println(boardID);
                    boolean created = line.split(" -BOOL- ")[1].equals("true");
                    try {
                        Board b = server.getBoardById(boardID);
                        size++;
                        HBox boardContent = new HBox(25);
                        Label boardLabel = new Label(b.getName());
                        Button copyKey = new Button();
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().
                                getResourceAsStream("/client/assets/key.png"))));
                        Button openBoard = new Button("Open Board");
                        Button delete = new Button("X");

                        prepareContent(boardLabel, copyKey, imageView, openBoard, delete, b, created);

                        boardContent.getChildren().addAll(boardLabel, copyKey, openBoard, delete);
                        boardsBox.getChildren().add(boardContent);
                    } catch (BadRequestException e) {
                        current = removeBoardFromClient(boardID, current);
                        e.printStackTrace();
                    }
                }
                scanner.close();
                if (!current.getName().equals(boardFilePath)) {
                    if (!current.getName().equals("boardsTemp.txt")) {
                        new File("boardsTemp.txt").delete();
                    }
                    new File(boardFilePath).delete();
                    System.out.println(current.getName());
                    System.out.println(current.renameTo(new File(boardFilePath)));
                    new File("boardsTest.txt").delete();
                }
                new File(boardFilePath).delete();
                current.renameTo(new File(boardFilePath));
                new File("boardsTest.txt").delete();
            boardsBox.setPrefSize(600, 225 * size);
            boardContainer.setContent(boardsBox);

            } catch (FileNotFoundException e) {
                System.out.println("No boards exist for client.");
            }
        } else {
            addAllBoards();
        }
    }

    /**
     * Removes a board from the client
     *
     * @param boardID the board id that wasn't found
     */
    private File removeBoardFromClient(long boardID, File current) {
        File inputFile = current;
        File tempFile = new File("boardsTemp.txt");
        if (inputFile.getName().equals(tempFile.getName())) {
            tempFile = new File("boardsTest.txt");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String lineToRemove = String.valueOf(boardID);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.split(" -BOOL- ")[0];
                if (trimmedLine.equals(lineToRemove)) continue;
                writer.write(currentLine);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    /**
     * Prepares each element in the hbox
     *
     * @param boardLabel label of board
     * @param copyKey    copy Key Button
     * @param imageView  the image for button
     * @param openBoard  the button to open the board
     * @param delete     the button to delete the board.
     * @param board      the board in question.
     * @param created    if the board was created by client or not.
     */
    private void prepareContent(Label boardLabel, Button copyKey, ImageView imageView, Button openBoard, Button delete, Board board, boolean created) {
        renameBoard(boardLabel, board);

        copyKey.setPrefSize(30, 40);
        copyKey.getStyleClass().add("imageButton");
        copyKey.setOnAction(event -> generateKey(board));

        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        imageView.setFitHeight(40);
        copyKey.setGraphic(imageView);


        openBoard.setPrefSize(100, 25);
        openBoard.getStyleClass().add("defaultButton");
        openBoard.setOnAction(event -> mainCtrl.showBoard(board));


        delete.getStyleClass().add("deleteButton");
        delete.setOnAction(event -> {
            if (created) {
                try {
                    server.send("/app/boardsDelete", board, session);
                } catch (WebApplicationException e) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                File current = removeBoardFromClient(board.getId(), new File(boardFilePath));
                if (!current.getName().equals(boardFilePath)) {
                    if (!current.getName().equals("boardsTemp.txt")) {
                        new File("boardsTemp.txt").delete();
                    }
                    new File(boardFilePath).delete();
                    current.renameTo(new File(boardFilePath));
                    new File("boardsTest.txt").delete();
                }
                show("Left " + board.getName() + "!");
                refresh();
            }

        });

    }

    /**
     * Generates a key for the multi board
     *
     * @param board the board for which a key is generated
     */
    private void generateKey(Board board) {
        String key = board.getName() + "-ID-" + board.getId();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(key);
        clipboard.setContent(content);
        show("Key Copied to Clipboard!");
    }

    /**
     * Joins a board
     */
    public void joinBoardMethod() {
        if (boardKey.getText().equals("")) {
            show("Please enter a key!");
            return;
        }
        String[] tokens = boardKey.getText().split("-ID-");
        if (tokens.length != 2) {
            show("Invalid Key!");
            return;
        }
        Board b;
        try {
            b = server.getBoardById(Long.parseLong(tokens[1]));
        } catch (BadRequestException e) {
            show("Invalid Key!");
            return;
        }

        File f = new File(boardFilePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean joined = checkBoard(b.getId(), f);
        if (!joined) {
            writeClientBoard(b, false);
        } else {
            show("Board has already been joined!");
        }

    }

    /**
     * Creates method for renaming board label
     *
     * @param boardLabel board's label
     * @param board      the board
     */
    private void renameBoard(Label boardLabel, Board board) {
        boardLabel.getStyleClass().add("collectionLabel");
        boardLabel.setPrefSize(350, 25);
        boardLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Rename a board
                TextInputDialog interact = new TextInputDialog(board.getName());
                interact.setHeaderText("Rename Board");
                interact.setContentText("Enter new name:");
                Optional<String> result = interact.showAndWait();
                if (result.isPresent()) {
                    String newName = result.get();
                    if (!newName.isEmpty()) {
                        board.setName(newName);
                        server.send("/app/boards", board, session);

                    }
                }
            }
        });
    }

    /**
     * Adds a board and sends to clients connected on websocket
     */
    public void addBoard() {
        // Add a Board
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
                    Board b = server.addBoard(boardN);
                    server.send("/app/boards", b, session);
                    writeClientBoard(b, true);
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
     * Writes the Client  board to the file
     *
     * @param board   the board being created/joined
     * @param created a boolean that determines whether the client created or joined the board
     */
    private void writeClientBoard(Board board, boolean created) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(boardFilePath, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileWriter.write(board.getId() + " -BOOL- " + created + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fileWriter.close();
                refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if board is already joined
     *
     * @param boardID the board id in question
     */
    private boolean checkBoard(long boardID, File current) {
        File inputFile = current;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String lineToRemove = String.valueOf(boardID);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.split(" -BOOL- ")[0];
                if (trimmedLine.equals(lineToRemove)) return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Shows the success for getting the key
     *
     * @param message the alert message
     */
    public static void show(String message) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Update!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Resets the overview of all Boards!
     */
    public void resetOverview() {
        try {
            server.send("/app/allBoardsDelete", new Board(), session);

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void addAllBoards() {
        int size = 0;
        VBox boardsBox = new VBox(25);
        for (Board board : server.getBoards()) {
            size++;
            HBox boardContent = new HBox(25);
            Label boardLabel = new Label(board.getName());
            Button copyKey = new Button();
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/client/assets/key.png"))));
            Button openBoard = new Button("Open Board");
            Button delete = new Button("X");

            prepareContent(boardLabel, copyKey, imageView, openBoard, delete, board, true);

            boardContent.getChildren().addAll(boardLabel, copyKey, openBoard, delete);
            boardsBox.getChildren().add(boardContent);
        }
    }

    /**
     * Switch back to welcome page
     */
    public void showWelcomePage() {
        mainCtrl.showWelcomePage();
    }

    /**
     * Switch to the admin login page
     */
    public void showAdminLoginPage() {
        mainCtrl.showAdminLogIn();
    }
}