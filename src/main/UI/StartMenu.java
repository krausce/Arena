package main.UI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Core.Connect4ComputerPlayer;

class StartMenu {
    private static final String TITLE = "Connect 4 Start Menu";
    private static final String INSTRUCTIONS = "Select which game modes you prefer.";
    private static final String PWD = (System.getProperty("os.name").toLowerCase().contains("windows")) ? "user.dir" : "pwd";
    private static int maxDepth;

    private StartMenu() {/**/}

    /**
     * Builds the entire opening menu and displays game options to the user.
     *
     * @throws FileNotFoundException warning to the compiler.
     */
    public static void display() throws FileNotFoundException {
        Stage startMenu = new Stage();
        BorderPane mainMenuLayout = new BorderPane();

        startMenu.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            Connect4GUI.quitGame = true;
        });

        startMenu.initModality(Modality.APPLICATION_MODAL);
        startMenu.setTitle(TITLE);
        startMenu.setMinWidth(500);
        startMenu.setMinHeight(400);

        VBox instructionsBox = new VBox();
        instructionsBox.paddingProperty().setValue(new Insets(10.0));
        instructionsBox.setAlignment(Pos.CENTER);

        Label startMenuInstructions = new Label();
        startMenuInstructions.setText(INSTRUCTIONS);
        startMenuInstructions.setAlignment(Pos.CENTER);
        instructionsBox.getChildren().add(startMenuInstructions);

        VBox topLayout = new VBox();
        topLayout.setSpacing(10);
        topLayout.setPadding(new Insets(15, 12, 15, 12));
        topLayout.setStyle("-fx-background-color: #336699;");

        HBox gameModeButtonsLayout = new HBox();
        gameModeButtonsLayout.setAlignment(Pos.CENTER);
        gameModeButtonsLayout.spacingProperty().setValue(15);
        Button humanOpponent = new Button("Human vs. Human");
        Button computerOpponent = new Button("Human vs. Robot");
        computerOpponent.setDefaultButton(true);
        gameModeButtonsLayout.getChildren().addAll(humanOpponent, computerOpponent);

        HBox userInputFields = new HBox();
        userInputFields.setAlignment(Pos.CENTER);
        userInputFields.spacingProperty().setValue(5);

        VBox player1 = new VBox();
        Tooltip nameFieldHint = new Tooltip("Enter your preferred name\n" +
                "  (i.e. Connect 4 King)");
        player1.setAlignment(Pos.CENTER);
        TextField player1NameField = new TextField();
        player1NameField.setTooltip(nameFieldHint);
        player1NameField.setPromptText("Player 1 Name:");
        player1.getChildren().addAll(player1NameField);

        VBox player2 = new VBox();
        player2.setAlignment(Pos.CENTER);
        TextField player2NameField = new TextField();
        player2NameField.setTooltip(nameFieldHint);
        player2NameField.setPromptText("Player 2 Name:");
        player2.getChildren().addAll(player2NameField);

        VBox difficultySelectionLayout = new VBox();
        difficultySelectionLayout.setSpacing(10);
        Label difficultySelection = new Label("Select a difficult option below");
        ToggleGroup difficultyOptions = new ToggleGroup();
        RadioButton normal = new RadioButton("Normal");
        normal.setSelected(true);
        normal.setToggleGroup(difficultyOptions);
        RadioButton hard = new RadioButton("Hard");
        hard.setToggleGroup(difficultyOptions);

        difficultySelectionLayout.getChildren().addAll(difficultySelection, normal, hard);

        humanOpponent.setOnMouseClicked(mouseEvent -> {
            userInputFields.getChildren().remove(difficultySelectionLayout);
            userInputFields.getChildren().add(player2NameField);
        });
        computerOpponent.setOnMouseClicked(mouseEvent -> {
            userInputFields.getChildren().remove(player2NameField);
            userInputFields.getChildren().add(difficultySelectionLayout);
        });

        userInputFields.getChildren().add(player1);

        topLayout.getChildren().addAll(gameModeButtonsLayout, userInputFields);
        if (computerOpponent.isArmed()) {
            userInputFields.getChildren().remove(player2NameField);
        } else {
            userInputFields.getChildren().remove(difficultySelectionLayout);
        }

        humanOpponent.onActionProperty().setValue(actionEvent -> {
            Connect4GUI.humanVsComputer = false;
            humanOpponent.setDisable(true);
            computerOpponent.setDisable(false);
        });
        computerOpponent.onActionProperty().setValue(actionEvent -> {
            Connect4GUI.humanVsComputer = true;
            computerOpponent.setDisable(true);
            humanOpponent.setDisable(false);
        });

        VBox centerLayout = new VBox();
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.spacingProperty().setValue(15);
        centerLayout.setStyle("-fx-background-color: lemonchiffon");

        HBox gamePlayMode = new HBox();
        gamePlayMode.setAlignment(Pos.CENTER);
        gamePlayMode.paddingProperty().setValue(new Insets(10.0));

        String guiImagePath = "img/GUIgameImage.png";
        Image guiImage = new Image(new FileInputStream(guiImagePath));
        Button guiGame = new Button("GUI Based Game");
        guiGame.setDefaultButton(true);
        guiGame.setGraphic(new ImageView(guiImage));

        String consoleImagePath = "img/ConsoleImage.png";
        Image consoleImage = new Image(new FileInputStream(consoleImagePath));
        Button consoleGame = new Button("Console Game");
        consoleGame.setGraphic(new ImageView(consoleImage));

        HBox bottomLayout = new HBox();
        bottomLayout.setSpacing(10);
        bottomLayout.setPadding(new Insets(15, 12, 15, 12));
        bottomLayout.setStyle("-fx-background-color: #336699;");

        String okButtonPath = "img/OK.png";
        Image okButtonImage = new Image(new FileInputStream(okButtonPath));
        Button okButton = new Button("OK");
        okButton.setGraphic(new ImageView(okButtonImage));


        String cancelButtonPath = "img/Shutdown.png";
        Image cancelButtonImage = new Image(new FileInputStream(cancelButtonPath));
        Button cancelButton = new Button("CANCEL / QUIT");
        cancelButton.setGraphic(new ImageView(cancelButtonImage));

        okButton.setOnAction(actionEvent -> {
            startMenu.close();
            maxDepth = (normal.isArmed()) ? 4 : 6;
            Connect4GUI.playerX.setName(player1NameField.getText());
            if (humanOpponent.isArmed()) {
                Connect4GUI.playerO.setName(player2NameField.getText());
            } else {
                Connect4GUI.playerO = new Connect4ComputerPlayer(new Connect4GuiInterface.Token(!Connect4GUI.playerX.getToken().red),
                        (Connect4GUI.playerX.getMarker().equals("X") ? "O" : "X"), maxDepth);
            }
            if (guiGame.isArmed()) {
                Connect4GUI.setConsoleGame(false);
            }
        });

        okButton.setOnMouseClicked(mouseEvent -> {
            startMenu.close();
            maxDepth = (normal.isArmed()) ? 4 : 6;
            Connect4GUI.playerX.setName(player1NameField.getText());
            if (humanOpponent.isArmed()) {
                Connect4GUI.playerO.setName(player2NameField.getText());
            } else {
                Connect4GUI.playerO = new Connect4ComputerPlayer(new Connect4GuiInterface.Token(!Connect4GUI.playerX.getToken().red),
                        (Connect4GUI.playerX.getMarker().equals("X") ? "O" : "X"), maxDepth);
            }
            if (guiGame.isArmed()) {
                Connect4GUI.setConsoleGame(false);
            }
        });

        cancelButton.setOnMouseClicked(mouseEvent -> {
            startMenu.close();
            Connect4GUI.quitGame = true;
        });

        bottomLayout.getChildren().addAll(okButton, cancelButton);
        gamePlayMode.getChildren().addAll(consoleGame, guiGame);
        centerLayout.getChildren().addAll(gamePlayMode, instructionsBox);
        if (!Connect4GUI.consoleGame) {
            centerLayout.getChildren().remove(consoleGame);
        }

        consoleGame.onMouseClickedProperty().setValue(mouseEvent -> {
            Connect4GUI.setConsoleGame(true);
            mainMenuLayout.topProperty().setValue(null);
        });
        guiGame.onActionProperty().setValue(actionEvent -> {
            Connect4GUI.setConsoleGame(false);
            mainMenuLayout.topProperty().setValue(topLayout);
        });
        mainMenuLayout.topProperty().setValue(topLayout);
        mainMenuLayout.centerProperty().setValue(centerLayout);
        mainMenuLayout.bottomProperty().setValue(bottomLayout);

        Scene mainMenuWindow = new Scene(mainMenuLayout);
        mainMenuLayout.getStylesheets().clear();
        startMenu.setScene(mainMenuWindow);
        startMenu.showAndWait();
    }
}
