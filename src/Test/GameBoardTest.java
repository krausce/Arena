package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Core.Connect4;
import main.Core.GameBoard;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardTest {
    private static final String MARKER = "X";
    private static final int COLUMN_SELECTION = 0;
    private String[][] testBoard;

    @BeforeEach
    void setup() {
        testBoard = new String[6][7];
    }

    @Test
    void initializeBoard() {
        assertThrows(NullPointerException.class, () -> Connect4.insertToken(testBoard, MARKER, COLUMN_SELECTION));
        GameBoard.initializeBoard(testBoard);
        assertTrue(Connect4.insertToken(testBoard, MARKER, COLUMN_SELECTION));
    }

    @Test
    void dropMarker() {
        assertThrows(NullPointerException.class, () -> GameBoard.dropMarker(testBoard, MARKER, COLUMN_SELECTION));
        GameBoard.initializeBoard(testBoard);
        assertTrue(GameBoard.dropMarker(testBoard, MARKER, COLUMN_SELECTION));
    }

    @Test
    void checkWinCondition() {
        int col = 0;
        GameBoard.initializeBoard(testBoard);
        // Horizontal win test
        Connect4.insertToken(testBoard, MARKER, col++);
        Connect4.insertToken(testBoard, "X", col++);
        Connect4.insertToken(testBoard, "X", col++);
        assertFalse(Connect4.won(testBoard, "X"));
        Connect4.insertToken(testBoard, "X", col);
        assertTrue(Connect4.won(testBoard, "X"));

        // Vertical win test
        col = 0;
        Connect4.initializeBoard(testBoard);
        Connect4.insertToken(testBoard, "X", col);
        Connect4.insertToken(testBoard, "X", col);
        Connect4.insertToken(testBoard, "X", col);
        assertFalse(Connect4.won(testBoard, "X"));
        Connect4.insertToken(testBoard, "X", col);
        assertTrue(Connect4.won(testBoard, "X"));

        // Diagonal L -> R and up
        Connect4.initializeBoard(testBoard);
        Connect4.insertToken(testBoard, "X", col++);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col++);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col++);
        assertFalse(Connect4.won(testBoard, "X"));
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col);
        assertTrue(Connect4.won(testBoard, "X"));

        // Diagonal L -> R and Down
        col = 0;
        Connect4.initializeBoard(testBoard);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col++);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col++);
        Connect4.insertToken(testBoard, "O", col);
        Connect4.insertToken(testBoard, "X", col++);
        assertFalse(Connect4.won(testBoard, "X"));
        Connect4.insertToken(testBoard, "X", col);
        assertTrue(Connect4.won(testBoard, "X"));
    }
}