package UI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Core.Connect4;
import Core.HumanPlayer;
import Core.Player;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Connect4GUI extends Application {

    public static final List<Rectangle> tokenSpaces = new ArrayList<>();
    static final Player playerX = new HumanPlayer(new Token(true));
    /**
     * This class runs and/or implements a GUI-Based Connect 4 game. This class reuses functionality from the CORE package.
     */
    private static final int GAME_BOARD_TILE_SIZE = 80;
    private static final int COLUMNS = Connect4.COLUMNS;
    private static final int ROWS = Connect4.ROWS;
    protected static final Token[][] gameBoard = new Token[COLUMNS][ROWS];
    private static final boolean SHOW_CONNECT4_MAIN_MENU = false;
    private static final String[][] mockGameBoard = new String[ROWS][COLUMNS];
    static boolean quitGame = false;
    static boolean isRedMove = true;
    static boolean humanVsComputer = true;
    static boolean consoleGame;
    static Player playerO = new HumanPlayer(new Token(false));
    private static int numberOfMovesMade = 0;
    private static Player currentPlayer = playerX;
    private final Pane tokenBase = new Pane();
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

    static Shape buildGameBoard() {
        Shape shape = new Rectangle((COLUMNS + 1) * GAME_BOARD_TILE_SIZE, (ROWS + 1) * GAME_BOARD_TILE_SIZE);
        Connect4.initializeBoard(mockGameBoard);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(((double) GAME_BOARD_TILE_SIZE / 2));
                circle.setCenterX(((double) GAME_BOARD_TILE_SIZE / 2));
                circle.setCenterY(((double) GAME_BOARD_TILE_SIZE / 2));
                circle.setTranslateX(x * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));
                circle.setTranslateY(y * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.CORNFLOWERBLUE);
        shape.setEffect(lighting);

        return shape;
    }

    public static Rectangle makeRectangleHighlighter(int column) {
        Rectangle gameBoardColumnHighlighter = new Rectangle(GAME_BOARD_TILE_SIZE, (ROWS + 1) * GAME_BOARD_TILE_SIZE);
        gameBoardColumnHighlighter.setTranslateX(column * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));
        gameBoardColumnHighlighter.setFill(Color.TRANSPARENT);

        gameBoardColumnHighlighter.setOnMouseEntered(e -> gameBoardColumnHighlighter.setFill(
                Color.rgb(200, 200, 50, 0.3)));
        gameBoardColumnHighlighter.setOnMouseExited(e -> gameBoardColumnHighlighter.setFill(Color.TRANSPARENT));

        return gameBoardColumnHighlighter;
    }

    private static void setGameBoardCell(int column, int row, Token token) {
        gameBoard[column][row] = token;
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

    private Parent createBaseGUI() {
        Pane gameBoardPane = new Pane();
        gameBoardPane.getChildren().add(tokenBase);

        Shape gameBoardShape = buildGameBoard();
        gameBoardPane.getChildren().add(gameBoardShape);
        makeColumnHighlighter();
        gameBoardPane.getChildren().addAll(tokenSpaces);

        return gameBoardPane;
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

    private void switchPlayers() {
        incrementMovesMade();
        setIsRedMove(!isRedMove);
        setCurrentPlayer((currentPlayer.equals(playerX)) ? playerO : playerX);
    }

    private void placeToken(Token token, int column) {
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

    private Optional<Token> getToken(int column, int row) {
        if (column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS) {
            return Optional.empty();
        }

        return Optional.ofNullable(gameBoard[column][row]);
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

    public static class Token extends Circle {
        public final boolean red;

        public Token(boolean red) {
            super(((double) GAME_BOARD_TILE_SIZE / 2), red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX((double) GAME_BOARD_TILE_SIZE / 2);
            setCenterY((double) GAME_BOARD_TILE_SIZE / 2);
        }

        public void resetColor() {
            super.setFill(Paint.valueOf(Color.TRANSPARENT.toString()));
        }
    }

    private static class AlertWindow {
        public static void display(String title, String message) {
            Stage actionWindow = new Stage();

            actionWindow.initModality(Modality.APPLICATION_MODAL);
            actionWindow.setTitle(title);
            actionWindow.setMinWidth(400);
            actionWindow.setMinHeight(300);

            Label label = new Label();
            label.setText(message);

            Button continuePlaying = new Button("Play Again");
            continuePlaying.setOnAction(e -> {
                quitGame = false;
                actionWindow.close();
            });

            Button quitPlaying = new Button("Quit");
            quitPlaying.setOnAction(e -> {
                quitGame = true;
                actionWindow.close();
            });

            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(continuePlaying, quitPlaying);
            buttonBox.setAlignment(Pos.CENTER);

            VBox layout = new VBox(20);
            layout.getChildren().addAll(label, buttonBox);
            layout.setAlignment(Pos.CENTER);

            Scene alertWindow = new Scene(layout);
            alertWindow.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    continuePlaying.fire();
                    keyEvent.consume();
                }
            });
            actionWindow.setScene(alertWindow);
            actionWindow.showAndWait();
        }
    }
}
