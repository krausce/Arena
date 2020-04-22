package main.Core;

public class GameBoard {

    public static final String[][] connect4Board = new String[Connect4.ROWS][Connect4.COLUMNS];

    public GameBoard() {
        initializeBoard();
    }

    protected static void initializeBoard() {
        initializeBoard(connect4Board);
    }

    /**
     * Fills the 2D String Array with " ".
     *
     * @param emptyBoard Connect 4 Game Board which needs to be initialized
     */
    public static void initializeBoard(String[][] emptyBoard) {
        for (int row = 0; row < Connect4.ROWS; row++) {
            for (int col = 0; col < Connect4.COLUMNS; col++) {
                emptyBoard[row][col] = " ";
            }
        }
    }

    /**
     * @param board           2D String Array representing the current game board state
     * @param marker          players marker
     * @param columnSelection int representing the column to validate
     * @return true if the column was not already filled, false otherwise indicating the token was not placed
     */
    public static boolean dropMarker(String[][] board, String marker, int columnSelection) {
        for (int row = 0; row < Connect4.ROWS; row++) {
            if (board[row][columnSelection].equals(" ")) {
                board[row][columnSelection] = marker;
                return true;
            }
        }
        return false; // No spaces were available in that column.
    }

    /**
     * @param board  check to see if a win condition exists
     * @param marker marker to check the win condition against
     * @return true if a win condition exists, false otherwise
     */
    protected static boolean checkWinCondition(String[][] board, String marker) {
        return (horizontalCheck(board, marker)) || ((verticalCheck(board, marker)) || diagonalCheck(board, marker));
    }

    /**
     * @param board 2D String Array representing the game board
     * @param token current players token
     * @return true if horizontal win condition exists, false otherwise
     */
    protected static boolean horizontalCheck(String[][] board, String token) {
        for (int r = 0; r < Connect4.ROWS; r++) {
            for (int c = 0; c < Connect4.COLUMNS - 3; c++) {
                if (board[r][c].equals(token) &&
                        board[r][c + 1].equals(token) &&
                        board[r][c + 2].equals(token) &&
                        board[r][c + 3].equals(token)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param board 2D String Array representing the game board
     * @param token current players token
     * @return true if vertical win condition exists, false otherwise
     */
    protected static boolean verticalCheck(String[][] board, String token) {
        for (int r = 0; r < Connect4.ROWS - 3; r++) {
            for (int c = 0; c < Connect4.COLUMNS; c++) {
                // Check if there are 4 stacked vertically
                if (board[r][c].equals(token) &&
                        board[r + 1][c].equals(token) &&
                        board[r + 2][c].equals(token) &&
                        board[r + 3][c].equals(token)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param board 2D String Array representing the game board
     * @param token current players token
     * @return true if diagonal (Left - Right and Up or Left - Right and Down) win condition exists, false otherwise
     */
    protected static boolean diagonalCheck(String[][] board, String token) {
        //check for win diagonally (upper left to lower right)
        for (int row = 0; row < Connect4.ROWS - 3; row++) { //0 to 2
            for (int col = 0; col < Connect4.COLUMNS - 3; col++) { //0 to 3
                if (board[row][col].equals(token) && board[row][col].equals(board[row + 1][col + 1]) &&
                        board[row + 1][col + 1].equals(board[row + 2][col + 2]) &&
                        board[row + 2][col + 2].equals(board[row + 3][col + 3])) {
                    return true;
                }
            }
        }

        //check for win diagonally (lower left to upper right)
        for (int row = 3; row < Connect4.ROWS; row++) { //3 to 5
            for (int col = 0; col < Connect4.COLUMNS - 3; col++) { //0 to 3
                if (board[row][col].equals(token) && board[row][col].equals(board[row - 1][col + 1]) &&
                        board[row - 1][col + 1].equals(board[row - 2][col + 2]) &&
                        board[row - 2][col + 2].equals(board[row - 3][col + 3])) {
                    return true;
                }
            }
        }

        return false;
    }
}
