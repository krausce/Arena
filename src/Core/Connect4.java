package Core;

import java.util.InputMismatchException;
import java.util.Scanner;

import static Core.GameBoard.checkWinCondition;
import static java.lang.System.out;

public class Connect4 {

    public static final int COLUMNS = 7;
    public static final int ROWS = 6;
    private static final String[][] gameBoard = GameBoard.connect4Board;
    private final Scanner in;
    private int gameMode;
    private Player player1;
    private Player player2;
    private int numberOfMovesMade = 0;
    private Core.Player currentPlayer;

    public Connect4() {
        in = null;
    }

    public Connect4(Scanner in) {
        this.in = in;
        GameBoard.initializeBoard();

        while (player1 == null || player2 == null) {
            try {
                this.startGame();
            } catch (IllegalArgumentException | InputMismatchException e) {
                Menus.displayMessage("Invalid game option selected, please try again.");
                in.nextLine();
                this.startGame();
            }
        }
    }

    public static void initializeBoard(String[][] emptyBoard) {
        GameBoard.initializeBoard(emptyBoard);
    }

    public static boolean insertToken(String[][] board, String token, int columnSelection) {
        return (columnSelection >= 0 && columnSelection < COLUMNS) && GameBoard.dropMarker(board, token, columnSelection);
    }

    /**
     * Displays the current board state to the players
     *
     * @param originalGameBoard current game state to be displayed
     */
    public static void displayBoard(String[][] originalGameBoard) {
        StringBuilder displayedBoard = new StringBuilder();
        displayedBoard.append("\n*************************\n");
        displayedBoard.append("*  1  2  3  4  5  6  7  *\n");
        displayedBoard.append("*************************\n");

        for (int r = ROWS - 1; r >= 0; r--) {
            for (int c = 0; c < COLUMNS; c++) {
                if (c == 0) {
                    displayedBoard.append("  ");
                }
                displayedBoard.append("| ").append(originalGameBoard[r][c]);
            }
            displayedBoard.append("|\n");
        }

        Menus.displayMessage(displayedBoard.toString());
    }

    public static boolean won(String[][] gameBoard, String marker) {
        return checkWinCondition(gameBoard, marker);
    }

    public static boolean isADraw(String[][] currentGameState) {
        final int row = currentGameState.length - 1;
        try {
            for (int col = 0; col < currentGameState[0].length; col++) {
                if (currentGameState[row][col].equals(" ")) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void startGame() {
        do {
            Menus.displayPlayerModeMenu();
            if (in.hasNextInt()) {
                gameMode = in.nextInt();
            } else {
                Menus.displayMessage("Invalid entry. Please try again.");
            }
        } while (gameMode < 1 || gameMode > 2);
        this.initializePlayers(gameMode);
        this.currentPlayer = randomlySelectFirstPlayer();
    }

    private void initializePlayers(int gameMode) {

        switch (gameMode) {
            case 1:
                player1 = new HumanPlayer(this.getPlayerName("Player 1"), randomlyAssignMarker(), in);
                player2 = new HumanPlayer(this.getPlayerName("Player 2"), (player1.getMarker().equals("X")) ? "O" : "X", in);
                break;
            case 2:
                player2 = new Connect4ComputerPlayer((randomlyAssignMarker()), selectMaxDepth());
                player1 = new HumanPlayer(this.getPlayerName("Player 1"), (player2.getMarker().equals("X")) ? "O" : "X", in);
                break;
            default:
                throw new IllegalArgumentException("Invalid game mode entry.");
        }
    }

    public Player randomlySelectFirstPlayer() {
        return (Math.round(Math.random()) == 0) ? player1 : player2;
    }

    private String randomlyAssignMarker() {
        return (Math.round(Math.random()) == 0) ? "X" : "O";
    }

    private String getPlayerName(String anonymousPlayer) {
        out.printf("%s please enter your preferred name: %n", anonymousPlayer);
        return this.in.next();
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfStringBuilder"})
    private int selectMaxDepth() {

        int difficultyLevel = 0;
        do {
            Menus.displayMaxDepthSelectionMenu();
            if (in.hasNextInt()) {
                difficultyLevel = in.nextInt();
            } else {
                Menus.displayMessage("Invalid input, please try again.");
                in.next();
                continue;
            }

            if (difficultyLevel < 1 || difficultyLevel > 2) {
                Menus.displayMessage("Invalid game option selected, please try again.");
            }
        } while (!(difficultyLevel == 1 || difficultyLevel == 2));


        return (difficultyLevel == 1) ? 8 : 10;
    }

    public String[][] getGameBoard() {
        return gameBoard;
    }

    public void makeMove() {
        currentPlayer.makeMove(this.getGameBoard());
    }

    /**
     * @return the player instance whose turn it currently is.
     */
    public Core.Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player newPlayer) {
        this.currentPlayer = newPlayer;
    }

    /**
     * @return true if all possible moves have been made, false otherwise
     */
    public boolean over() {
        return numberOfMovesMade == ROWS * COLUMNS;
    }

    /**
     * Swaps out the player currently making their next move
     */
    public void nextPlayer() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }

    public boolean won(String marker) {
        return checkWinCondition(this.getGameBoard(), marker);
    }

    public void incrementNumberOfMovesMade() {
        this.numberOfMovesMade++;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getNumberOfMovesMade() {
        return numberOfMovesMade;
    }

    public void setNumberOfMovesMade(int numberOfMovesMade) {
        this.numberOfMovesMade = numberOfMovesMade;
    }
}
