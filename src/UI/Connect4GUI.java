package UI;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Core.Connect4;
import Core.HumanPlayer;
import Core.Player;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Connect4GUI extends Application implements Connect4GuiInterface {

    static final Player playerX = new HumanPlayer(new Token(true));
    private static final boolean SHOW_CONNECT4_MAIN_MENU = false;
    static boolean quitGame = false;
    static boolean isRedMove = true;
    static boolean humanVsComputer = true;
    static boolean consoleGame;
    static Player playerO = new HumanPlayer(new Token(false));
    private static int numberOfMovesMade = 0;
    private static Player currentPlayer = playerX;
    protected Stage mainStage;

    public static void main(String[] args) {
        try {
            if (Integer.parseInt(args[0]) == 2) {
                setConsoleGame(false);
            }
        } catch (Exception ignored) { /**/ }
        launch(args);
    }

    private static void incrementMovesMade() {
        numberOfMovesMade++;
    }

    private static void resetNumberOfMovesMade() {
        numberOfMovesMade = 0;
    }

    public static void setConsoleGame(boolean consoleGame) {
        Connect4GUI.consoleGame = consoleGame;
    }

    private static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    private static void setIsRedMove(boolean newRedMove) {
        isRedMove = newRedMove;
    }

    /**
     * Main launch point for entire graphical application version
     *
     * @param stage base layer for all other Java FX graphical components to be added
     * @throws FileNotFoundException warning to the compiler
     */
    @Override
    public void start(Stage stage) throws FileNotFoundException {
        StartMenu.display();
        if (quitGame) {
            System.exit(0);
        }
        if (consoleGame) {
            Connect4TextConsole.main(new String[]{String.valueOf(SHOW_CONNECT4_MAIN_MENU)});
        } else {
            mainStage = stage;
            playGame();
        }
    }

    private void playGame() {
        mainStage.setTitle("Connect 4");
        mainStage.setScene(new Scene(createBaseGUI()));
        mainStage.show();
    }

    private void switchPlayers() {
        incrementMovesMade();
        setIsRedMove(!isRedMove);
        setCurrentPlayer((currentPlayer.equals(playerX)) ? playerO : playerX);
    }

    @Override
    public void makeColumnHighlighter() {

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle gameBoardColumnHighlighter = makeRectangleHighlighter(x);

            final int column = x;
            gameBoardColumnHighlighter.setOnMouseClicked(mouseEvent -> mouseClickHandler(column));
            tokenSpaces.add(gameBoardColumnHighlighter);
        }
    }

    @Override
    public void mouseClickHandler(int column) {
        if (currentPlayer instanceof HumanPlayer && humanVsComputer) {
            placeToken(new Token(currentPlayer.getToken().red), column);
            Connect4.insertToken(mockGameBoard, currentPlayer.getMarker(), column);
            switchPlayers();
            placeToken(new Token(currentPlayer.getToken().red), playerO.makeMove(mockGameBoard));
            switchPlayers();
        } else {
            placeToken(new Token(currentPlayer.getToken().red), column);
            Connect4.insertToken(mockGameBoard, currentPlayer.getMarker(), column);
            switchPlayers();
        }
    }

    @Override
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

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), token);
        animation.setToY(row * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));
        animation.play();

        if (gameWon(column, currentRow)) {
            gameOver(true);
        } else if (numberOfMovesMade == ROWS * COLUMNS) {
            gameOver(false);
        }
    }

    private boolean gameWon(int column, int row) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        return checkRangeForWin(vertical) || checkRangeForWin(horizontal) || checkRangeForWin(diagonal1) || checkRangeForWin(diagonal2);
    }

    private boolean checkRangeForWin(List<Point2D> points) {
        int tokenCount = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Token token = getToken(column, row).orElse(new Token(!isRedMove));
            if (token.red == isRedMove) {
                tokenCount++;
                if (tokenCount == 4) {
                    return true;
                }
            } else {
                tokenCount = 0;
            }
        }

        return false;
    }

    private void gameOver(boolean gameWon) {
        if (gameWon) {
            AlertWindow.display("Game Over", String.format("Winner: %s", (isRedMove) ? playerX.getName() : playerO.getName()));
        } else {
            AlertWindow.display("Game Over", "Game ended in a draw.");
        }
        if (quitGame) {
            mainStage.close();
            System.exit(0);
        } else {
            mainStage.close();
            resetNumberOfMovesMade();
            resetBoard();
            mainStage.setScene(new Scene(createBaseGUI()));
            mainStage.show();
        }
    }

    private void resetBoard() {
        for (int col = 0; col < COLUMNS; col++) {
            for (int token = 0; token < ROWS; token++) {
                if (gameBoard[col][token] != null) {
                    gameBoard[col][token].resetColor();
                    setGameBoardCell(col, token, null);
                }
            }
        }
    }

}
