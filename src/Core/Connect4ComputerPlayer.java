package Core;


import java.util.ArrayList;
import java.util.List;

import UI.Connect4GUI;

import static java.lang.System.arraycopy;

@SuppressWarnings("DuplicatedCode")
public class Connect4ComputerPlayer implements Player {

    private static final String ROBOT_NAME = "Robinson Robot 2.0";
    private static final int[][] evaluationTable = {{3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}};
    private static final int[] columnExplorationOrder = initializeColumnSelectionOrderArray(7);
    /**
     * Maximum game tree depth to search at.
     */
    private static int maxDepth = 4;
    private static int staticBestMoveColumn;
    private static double staticBestMoveScore;
    private final String marker;
    private int numberOfMovesMade = 0;
    private Connect4GUI.Token token;

    public Connect4ComputerPlayer(Connect4GUI.Token token, String marker) {
        this.marker = marker;
        this.token = token;
    }

    public Connect4ComputerPlayer(Connect4GUI.Token token, String marker, int maxDepth) {
        this(token, marker);
        setMaxDepth(maxDepth);
    }

    public Connect4ComputerPlayer(String marker, int maxDepth) {
        this.marker = marker;
        setMaxDepth(maxDepth);
    }

    public static void setStaticBestMoveColumn(int staticBestMoveColumn) {
        Connect4ComputerPlayer.staticBestMoveColumn = staticBestMoveColumn;
    }

    public static void setMaxDepth(int maxDepth) {
        Connect4ComputerPlayer.maxDepth = maxDepth;
    }

    public static void setStaticstaticBestMoveScore(double staticBestMoveScore) {
        Connect4ComputerPlayer.staticBestMoveScore = staticBestMoveScore;
    }

    private static int[] initializeColumnSelectionOrderArray(int numColumns) {
        final int[] temp = new int[numColumns];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (temp.length / 2) + (1 - 2 * (i % 2)) * (i + 1) / 2;
        }
        return temp;
    }

    private static ArrayList<Integer> getAvailableMoves(String[][] copyGameBoard) {
        ArrayList<Integer> moves = new ArrayList<>();
        final int ROW = copyGameBoard.length - 1;
        for (int col = 0; col < copyGameBoard[0].length; col++) {
            if (copyGameBoard[ROW][col].equals(" ")) {
                moves.add(col);
            }
        }
        return moves;
    }

    private static String otherMarker(String marker) {
        return (marker.equals("X")) ? "O" : "X";
    }

    private static String[][] copyGameBoard(String[][] boardToCopy) {
        int rows = boardToCopy.length;
        int columns = boardToCopy[0].length;
        String[][] copy = new String[rows][columns];
        for (int r = 0; r < rows; r++) {
            arraycopy(boardToCopy[r], 0, copy[r], 0, columns);
        }
        return copy;
    }

    /**
     * This is a basic implementation of the Negamax algorithm https://en.wikipedia.org/wiki/Negamax with alpha beta pruning. This algorithm
     * first looks for a potential move that will win the game for either the computer or the opponent and then makes a play in that column.
     * If no immediate win condition is detected, the minimax algorithm is then utilized to select the best move for the computer which
     * minimizes the win potential for the opponent.
     *
     * @param originalGameBoard Connect 4 game board
     */
    @Override
    public int makeMove(String[][] originalGameBoard) {
        double score;
        for (int iter = 0; iter < originalGameBoard[0].length; iter++) {
            String[][] copy = copyGameBoard(originalGameBoard);
            if (this.isWinningMove(copy, this.marker, columnExplorationOrder[iter]) ||
                    this.isWinningMove(copy, otherMarker(this.marker), columnExplorationOrder[iter])) {
                setStaticBestMoveColumn(columnExplorationOrder[iter]);
                break;
            }

            Connect4.insertToken(copy, marker, columnExplorationOrder[iter]);
            score = negamax(copy, this.getMarker(), 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
            setStaticstaticBestMoveScore(Math.max(staticBestMoveScore, score));
            setStaticBestMoveColumn((staticBestMoveScore == score) ? columnExplorationOrder[iter] : staticBestMoveColumn);
        }
        Connect4.insertToken(originalGameBoard, getMarker(), staticBestMoveColumn);
        setStaticstaticBestMoveScore(Double.NEGATIVE_INFINITY);
        return staticBestMoveColumn;
    }

    private boolean isWinningMove(String[][] gameBoardCopy, String marker, int col) {
        Connect4.insertToken(gameBoardCopy, marker, col);
        boolean won = Connect4.won(gameBoardCopy, marker);
        this.undoMove(gameBoardCopy, col);
        return won;
    }

    private void undoMove(String[][] gameBoardCopy, int col) {
        for (int row = (gameBoardCopy.length - 1); row >= 0; row--) {
            if (!gameBoardCopy[row][col].equals(" ")) {
                gameBoardCopy[row][col] = " ";
                break;
            }
        }
    }

    private double negamax(String[][] copyOfGameBoard, String marker, int depth, double alpha, double beta) {
        double score = 0;
        if (depth > maxDepth || Connect4.won(copyOfGameBoard, marker)) {
            return evaluteTestGameState(copyOfGameBoard, depth);
        }
        List<Integer> possibleMoves = getAvailableMoves(copyOfGameBoard);
        for (Integer childNode : possibleMoves) {
            Connect4.insertToken(copyOfGameBoard, marker, columnExplorationOrder[childNode]);
            score = -negamax(copyGameBoard(copyOfGameBoard), otherMarker(marker), depth + 1, -beta, -alpha);
            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break;
            }
        }
        return score;
    }

    private double evaluteTestGameState(String[][] copyGameBoard, int depth) {
        double baseScore = 138;
        double overallScore = 0;
        for (int r = 0; r < copyGameBoard.length; r++) {
            for (int c = 0; c < copyGameBoard[0].length; c++) {
                overallScore += (copyGameBoard[r][c].equals(this.getMarker())) ? evaluationTable[r][c] : -evaluationTable[r][c];
            }
        }

        return (baseScore + overallScore) * (((maxDepth - depth) > 0) ? (maxDepth - depth) : 1);
    }

    public String getMarker() {
        return marker;
    }

    @Override
    public void incrementNumberOfMovesMade() {
        this.numberOfMovesMade++;
    }

    @Override
    public Connect4GUI.Token getToken() {
        return this.token;
    }

    public String getRobotName() {
        return ROBOT_NAME;
    }

    @Override
    public String getName() {
        return this.getRobotName();
    }

    /*
     * Robot Player has a constant name ROBOT_NAME
     * */
    @Override
    public void setName(String text) {
    }
}
