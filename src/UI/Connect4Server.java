package UI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Core.Connect4;
import Core.GameBoard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import static UI.Connect4ServerConstants.CONTINUE;
import static UI.Connect4ServerConstants.DRAW;
import static UI.Connect4ServerConstants.LOCALHOST;
import static UI.Connect4ServerConstants.PLAYER_1;
import static UI.Connect4ServerConstants.PLAYER_1_TOKEN;
import static UI.Connect4ServerConstants.PLAYER_1_WON;
import static UI.Connect4ServerConstants.PLAYER_2;
import static UI.Connect4ServerConstants.PLAYER_2_TOKEN;
import static UI.Connect4ServerConstants.PLAYER_2_WON;

/**
 * Connect4Server allows two players to play against each other remotely, though at the moment only on the same computer.
 *
 * @author Chris Kraus
 * @version 1.0
 */
public class Connect4Server extends Application {
    private static final Logger SERVER_LOGGER = Logger.getLogger(Connect4Server.class.getName());
    private static int sessionNumber = 1;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is the launch point for the server application GUI which just displays simple text messages at the moment.
     *
     * @param primaryStage the stage for displaying the text area.
     */
    @Override
    public void start(Stage primaryStage) {
        TextArea serverLogWindow = new TextArea();

        primaryStage.setTitle("Connect4Server");
        primaryStage.setScene(new Scene(serverLogWindow));
        primaryStage.show();

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(LOCALHOST);
                Platform.runLater(() -> serverLogWindow.appendText(new Date() + ": Server started at socket " + LOCALHOST + '\n'));

                while (true) {
                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + "Waiting for players to join session " +
                            sessionNumber + '\n'));

                    Socket firstPlayer = serverSocket.accept();
                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + "Player 1 joined session " +
                            sessionNumber + '\n'));
                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + "Player 1 IP Address: " +
                            firstPlayer.getInetAddress().getHostAddress() + '\n'));
                    new DataOutputStream(firstPlayer.getOutputStream()).writeInt(PLAYER_1);


                    Socket secondPlayer = serverSocket.accept();
                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + "Player 2 joined session " +
                            sessionNumber + '\n'));
                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + "Player 2 IP Address: " +
                            secondPlayer.getInetAddress().getHostAddress() + '\n'));
                    new DataOutputStream(secondPlayer.getOutputStream()).writeInt(PLAYER_2);

                    Platform.runLater(() -> serverLogWindow.appendText(new Date() + ": Start a thread for session " + sessionNumber++ + '\n'));
                    new Thread(new SessionHandler(firstPlayer, secondPlayer)).start();
                }
            } catch (IOException e) {
                SERVER_LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }).start();
    }

    /**
     * SessionHandler is an inner class that holds all of the game play logical functionality required to play Connect 4.
     */
    private static class SessionHandler implements Runnable {
        private static final Logger SESSION_LOGGER = Logger.getLogger(SessionHandler.class.getName());
        private final String[][] serverGameBoard = GameBoard.connect4Board;
        private final DataInputStream[] fromPlayers = new DataInputStream[2];
        private final DataOutputStream[] toPlayers = new DataOutputStream[2];
        private int currentPlayer = 0;
        private boolean gameWon = false;
        private boolean gameIsADraw = false;

        public SessionHandler(Socket firstPlayer, Socket secondPlayer) {
            Socket[] playerSockets = new Socket[2];
            playerSockets[0] = firstPlayer;
            playerSockets[1] = secondPlayer;
            Connect4.initializeBoard(serverGameBoard);

            try {
                for (int i = 0; i < playerSockets.length; i++) {
                    fromPlayers[i] = new DataInputStream(playerSockets[i].getInputStream());
                    toPlayers[i] = new DataOutputStream(playerSockets[i].getOutputStream());
                }
            } catch (IOException e) {
                SESSION_LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        /**
         * Given that SessionHandler is an implementation of Runnable, this method is required to be overridden to provide the desired
         * runtime functionality.
         */
        @Override
        public void run() {
            try {
                toPlayers[currentPlayer].writeInt(PLAYER_1);
                while (true) {
                    int columnSelected = fromPlayers[currentPlayer].readInt();
                    Connect4.insertToken(serverGameBoard, (currentPlayer == 0 ? PLAYER_1_TOKEN : PLAYER_2_TOKEN), columnSelected);
                    checkGameOverConditions(currentPlayer == 0 ? PLAYER_1_TOKEN : PLAYER_2_TOKEN);
                    switchPlayers();
                    if (gameWon) {
                        updateGameStatus(toPlayers[0], toPlayers[1], (currentPlayer == 1 ? PLAYER_1_WON : PLAYER_2_WON));
                        sendMove(toPlayers[currentPlayer], columnSelected);
                        break;
                    } else if (gameIsADraw) {
                        updateGameStatus(toPlayers[0], toPlayers[1], DRAW);
                        sendMove(toPlayers[currentPlayer], columnSelected);
                        break;
                    } else {
                        sendMove(toPlayers[currentPlayer], CONTINUE);
                        sendMove(toPlayers[currentPlayer], columnSelected);
                    }
                }
            } catch (IOException e) {
                SESSION_LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        private void switchPlayers() {
            currentPlayer = (currentPlayer + 1) % 2;
        }

        private void updateGameStatus(DataOutputStream firstPlayerOut, DataOutputStream secondPlayerOut, int gameStatusUpdate) throws IOException {
            firstPlayerOut.writeInt(gameStatusUpdate);
            secondPlayerOut.writeInt(gameStatusUpdate);
        }

        private void sendMove(DataOutputStream out, int dataOut) throws IOException {
            out.writeInt(dataOut);
        }

        private void checkGameOverConditions(String marker) {
            gameWon = Connect4.won(serverGameBoard, marker);
            gameIsADraw = Connect4.isADraw(serverGameBoard);
        }
    }
}
