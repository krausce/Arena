package main.UI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Core.Connect4;

import static main.Core.Connect4ServerConstants.CONTINUE;
import static main.Core.Connect4ServerConstants.DRAW;
import static main.Core.Connect4ServerConstants.HOST;
import static main.Core.Connect4ServerConstants.LOCALHOST;
import static main.Core.Connect4ServerConstants.PLAYER_1;
import static main.Core.Connect4ServerConstants.PLAYER_1_TOKEN;
import static main.Core.Connect4ServerConstants.PLAYER_1_WON;
import static main.Core.Connect4ServerConstants.PLAYER_2;
import static main.Core.Connect4ServerConstants.PLAYER_2_TOKEN;
import static main.Core.Connect4ServerConstants.PLAYER_2_WON;

/**
 * Connect4Client is a client application for the network distributed version of Connect 4. There will be exactly two instances of this
 * class per SessionHandler instance in Connect4Server.
 *
 * @author Chris Kraus
 * @version 1.0
 */
public class Connect4Client extends Application implements Connect4GuiInterface {
    private static final Logger CLIENT_LOGGER = Logger.getLogger(Connect4Client.class.getName());
    private final Label titleLabel = new Label();
    private final Label statusLabel = new Label();
    private boolean myTurn = false;
    private String myToken = " ";
    private int columnSelected;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private boolean quitGame;
    private boolean waiting = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connect4.initializeBoard(mockGameBoard);
        BorderPane clientLayout = new BorderPane();
        clientLayout.setTop(titleLabel);
        clientLayout.setCenter(createBaseGUI());
        clientLayout.setBottom(statusLabel);
        primaryStage.setScene(new Scene(clientLayout));
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket clientSocket = new Socket(HOST, LOCALHOST);
            fromServer = new DataInputStream(clientSocket.getInputStream());
            toServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (Exception e) {
            CLIENT_LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        new Thread(() -> {
            try {
                int player = fromServer.readInt();
                switch (player) {
                    case PLAYER_1:
                        myToken = PLAYER_1_TOKEN;
                        Platform.runLater(() -> titleLabel.setText("Player 1 (RED)"));
                        Platform.runLater(() -> statusLabel.setText("Waiting for Player 2 (YELLOW)"));
                        fromServer.readInt(); // Placeholder for a "busy waiting" status until the other player joins.
                        Platform.runLater(() -> statusLabel.setText("Player 2 (Yellow) has joined. Player 1 (RED) begins first."));
                        myTurn = true;
                        break;
                    case PLAYER_2:
                        myToken = PLAYER_2_TOKEN;
                        Platform.runLater(() -> titleLabel.setText("Player 2 (YELLOW)"));
                        Platform.runLater(() -> statusLabel.setText("Waiting for Player 1 (RED) to move."));
                        break;
                    default:
                        throw new IllegalArgumentException("Bad server input. Server input value was: " + player);
                }

                while (!quitGame) {
                    if (player == PLAYER_1) {
                        waitForCurrentPlayerMove();
                        sendMove();
                        receiveInfoFromServer();
                    } else {
                        receiveInfoFromServer();
                        waitForCurrentPlayerMove();
                        sendMove();
                    }
                }
            } catch (Exception e) {
                CLIENT_LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }).start();
    }

    private void waitForCurrentPlayerMove() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }
        waiting = true;
    }

    private void receiveInfoFromServer() throws IOException {
        int currentGameState = fromServer.readInt();

        switch (currentGameState) {
            case PLAYER_1_WON:
                quitGame = true;
                Platform.runLater(() -> statusLabel.setText((myToken.equals("X") ?
                        "Congratulations Player 1, you've won the game!" :
                        "Player 1 (RED) has won the game, better luck next time.")));
                if (myToken.equals(PLAYER_2_TOKEN)) {
                    receiveMove();
                }
                break;
            case PLAYER_2_WON:
                quitGame = true;
                Platform.runLater(() -> statusLabel.setText((myToken.equals(PLAYER_2_TOKEN) ?
                        "Congratulations Player 2, you've won the game!" :
                        "Player 2 (YELLOW) has won the game, better luck next time.")));
                if (myToken.equals(PLAYER_1_TOKEN)) {
                    receiveMove();
                }
                break;
            case DRAW:
                quitGame = true;
                Platform.runLater(() -> statusLabel.setText("The game has ended in a draw. No winner determined."));
                if (myToken.equals("X")) {
                    receiveMove();
                }
                break;
            case CONTINUE:
                receiveMove();
                Platform.runLater(() -> statusLabel.setText((myToken.equals(PLAYER_1_TOKEN) ?
                        "RED, it is now your turn" : "YELLOW, it is now your turn")));
                myTurn = true;
                break;
            default:
                throw new IllegalArgumentException("Bad input from the server. Server input was: " + currentGameState);
        }
    }

    private void sendMove() throws IOException {
        toServer.writeInt(columnSelected);
    }

    private void receiveMove() throws IOException {
        int otherPlayerMove = fromServer.readInt();
        Platform.runLater(() -> placeToken(new Token(!myToken.equals("X")), otherPlayerMove));
    }

    public void makeColumnHighlighter() {

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle gameBoardColumnHighlighter = makeRectangleHighlighter(x);

            final int column = x;
            gameBoardColumnHighlighter.setOnMouseClicked(mouseEvent -> mouseClickHandler(column));
            tokenSpaces.add(gameBoardColumnHighlighter);
        }
    }

    public void mouseClickHandler(int column) {
        if (myTurn) {
            placeToken(new Token(myToken.equals(PLAYER_1_TOKEN)), column);
            Connect4.insertToken(mockGameBoard, myToken, column);
            myTurn = false;
            columnSelected = column;
            statusLabel.setText("Waiting for the other player to make a move.");
            waiting = false;
        }
    }

    public void placeToken(Token token, int column) {
        int row = ROWS - 1;
        do {
            if (getToken(column, row).isEmpty()) {
                break;
            }
            row--;
        } while (row >= 0);

        if (row < 0) {
            return;
        }

        setGameBoardCell(column, row, token);
        tokenBase.getChildren().add(token);
        token.setTranslateX(column * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), token);
        animation.setToY(row * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));
        animation.play();
    }
}
