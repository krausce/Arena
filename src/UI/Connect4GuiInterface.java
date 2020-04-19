package UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Core.Connect4;
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

public interface Connect4GuiInterface {

    List<Rectangle> tokenSpaces = new ArrayList<>();
    int GAME_BOARD_TILE_SIZE = 80;
    int COLUMNS = Connect4.COLUMNS;
    int ROWS = Connect4.ROWS;
    String[][] mockGameBoard = new String[ROWS][COLUMNS];
    Token[][] gameBoard = new Token[COLUMNS][ROWS];
    Pane tokenBase = new Pane();

    static Shape buildGameBoard() {
        Shape shape = new Rectangle((COLUMNS + 1) * Connect4GUI.GAME_BOARD_TILE_SIZE, (ROWS + 1) * Connect4GUI.GAME_BOARD_TILE_SIZE);
        Connect4.initializeBoard(mockGameBoard);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(((double) Connect4GUI.GAME_BOARD_TILE_SIZE / 2));
                circle.setCenterX(((double) Connect4GUI.GAME_BOARD_TILE_SIZE / 2));
                circle.setCenterY(((double) Connect4GUI.GAME_BOARD_TILE_SIZE / 2));
                circle.setTranslateX(x * (Connect4GUI.GAME_BOARD_TILE_SIZE + 5) + ((double) Connect4GUI.GAME_BOARD_TILE_SIZE / 4));
                circle.setTranslateY(y * (Connect4GUI.GAME_BOARD_TILE_SIZE + 5) + ((double) Connect4GUI.GAME_BOARD_TILE_SIZE / 4));

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

    default Parent createBaseGUI() {
        Pane gameBoardPane = new Pane();
        gameBoardPane.getChildren().add(tokenBase);

        Shape gameBoardShape = buildGameBoard();
        gameBoardPane.getChildren().add(gameBoardShape);
        makeColumnHighlighter();
        gameBoardPane.getChildren().addAll(Connect4GUI.tokenSpaces);

        return gameBoardPane;
    }

    void makeColumnHighlighter();


    default Rectangle makeRectangleHighlighter(int column) {
        Rectangle gameBoardColumnHighlighter = new Rectangle(GAME_BOARD_TILE_SIZE, (ROWS + 1) * GAME_BOARD_TILE_SIZE);
        gameBoardColumnHighlighter.setTranslateX(column * (GAME_BOARD_TILE_SIZE + 5) + ((double) GAME_BOARD_TILE_SIZE / 4));
        gameBoardColumnHighlighter.setFill(Color.TRANSPARENT);

        gameBoardColumnHighlighter.setOnMouseEntered(e -> gameBoardColumnHighlighter.setFill(
                Color.rgb(200, 200, 50, 0.3)));
        gameBoardColumnHighlighter.setOnMouseExited(e -> gameBoardColumnHighlighter.setFill(Color.TRANSPARENT));

        return gameBoardColumnHighlighter;
    }

    void mouseClickHandler(int column);

    void placeToken(Token token, int column);

    default Optional<Token> getToken(int column, int row) {
        if (column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS) {
            return Optional.empty();
        }

        return Optional.ofNullable(Connect4GUI.gameBoard[column][row]);
    }


    default void setGameBoardCell(int column, int row, Token token) {
        gameBoard[column][row] = token;
    }

    class Token extends Circle {
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

    class AlertWindow {

        private AlertWindow() {/**/}

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
                Connect4GUI.quitGame = false;
                actionWindow.close();
            });

            Button quitPlaying = new Button("Quit");
            quitPlaying.setOnAction(e -> {
                Connect4GUI.quitGame = true;
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
