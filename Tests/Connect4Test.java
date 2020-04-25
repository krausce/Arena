import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import main.Core.Connect4;
import main.Core.Connect4ComputerPlayer;
import main.Core.Connect4HumanPlayer;
import main.UI.Connect4GuiInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4Test {

    private static final PrintStream systemOutputBackup = System.out;
    private static final PrintStream systemErrorOutputBackup = System.err;
    private static final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream testErrorOutput = new ByteArrayOutputStream();
    private static final InputStream systemInputBackup = System.in;
    private Connect4 game;

    @BeforeAll
    static void setupInputOutput() {
        System.setOut(new PrintStream(testOutput));
        System.setErr(new PrintStream(testErrorOutput));
    }

    @AfterAll
    static void resetInputOutput() {
        System.setOut(systemOutputBackup);
        System.setErr(systemErrorOutputBackup);
        System.setIn(systemInputBackup);
    }

    @BeforeEach
    void setUp() {
        game = new Connect4();
    }

    @Test
    void Connect4() {
        Connect4 testGame = new Connect4();
        assertEquals(0, testGame.getGameMode());
        assertNotNull(testGame);
        assertNull(testGame.getScanner());
        assertNull(testGame.getCurrentPlayer());
        assertNull(testGame.getPlayer1());
        assertNull(testGame.getPlayer2());

        ByteArrayInputStream testInput = new ByteArrayInputStream("1\nTEST\nTEST".getBytes());
        System.setIn(testInput);

        testGame = new Connect4(new Scanner(System.in));
        assertNotNull(testGame.getPlayer1());
        assertNotNull(testGame.getPlayer2());
        assertNotNull(testGame.getCurrentPlayer());
        System.setIn(systemInputBackup);

        testInput = new ByteArrayInputStream("a\n1\nTEST\nTEST".getBytes());
        System.setIn(testInput);
        new Connect4(new Scanner(System.in));
        assertTrue(testOutput.toString().contains("Invalid entry. Please try again."));

        testInput = new ByteArrayInputStream("2\na\n0\n3\n1\nTEST".getBytes());
        System.setIn(testInput);
        testGame = new Connect4(new Scanner(System.in));
        assertTrue(testOutput.toString().contains("Invalid input, please try again."));
        assertTrue(testOutput.toString().contains("Invalid game option selected, please try again."));
        assertTrue(testOutput.toString().contains("Invalid game option selected, please try again."));
        assertTrue(testGame.getPlayer2() instanceof Connect4ComputerPlayer);
        assertEquals(8, Connect4ComputerPlayer.getMaxDepth());

        testInput = new ByteArrayInputStream("2\n2\nTEST".getBytes());
        System.setIn(testInput);
        new Connect4(new Scanner(System.in));
        assertEquals(10, Connect4ComputerPlayer.getMaxDepth());
    }

    @Test
    void nextPlayer() {
        ByteArrayInputStream testInput = new ByteArrayInputStream("a\n1\nplayer1\nplayer2".getBytes());
        System.setIn(testInput);
        Connect4 testGame = new Connect4(new Scanner(System.in));
        String firstPlayer = testGame.getCurrentPlayer().getName();
        testGame.nextPlayer();
        assertNotEquals(firstPlayer, testGame.getCurrentPlayer());
    }

    @Test
    void displayBoard() {
        final String[][] testBoard = new String[6][7];
        Connect4.initializeBoard(testBoard);
        String displayedBoard = Connect4.makeConnect4Board(testBoard);
        Connect4.displayBoard(testBoard);
        assertTrue(testOutput.toString().contains(displayedBoard));
    }

    @Test
    void initializeBoard() {
        final String[][] testBoard = new String[6][7];
        assertNull(testBoard[0][0]);
        Connect4.initializeBoard(testBoard);
        assertEquals(" ", testBoard[0][0]);
    }

    @Test
    void insertToken() {
        Connect4.initializeBoard(game.getGameBoard());
        assertTrue(Connect4.insertToken(game.getGameBoard(), "X", 0));
        assertEquals("X", game.getGameBoard()[0][0]);

        assertFalse(Connect4.insertToken(game.getGameBoard(), "X", 8));
        assertFalse(Connect4.insertToken(game.getGameBoard(), "X", -1));
    }

    @Test
    void won() {
        int col = 0;
        Connect4.initializeBoard(game.getGameBoard());
        // Horizontal win test
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        assertFalse(game.won("X"));
        Connect4.insertToken(game.getGameBoard(), "X", col);
        assertTrue(game.won("X"));

        // Vertical win test
        col = 0;
        Connect4.initializeBoard(game.getGameBoard());
        Connect4.insertToken(game.getGameBoard(), "X", col);
        Connect4.insertToken(game.getGameBoard(), "X", col);
        Connect4.insertToken(game.getGameBoard(), "X", col);
        assertFalse(game.won("X"));
        Connect4.insertToken(game.getGameBoard(), "X", col);
        assertTrue(game.won("X"));

        // Diagonal L -> R and up
        Connect4.initializeBoard(game.getGameBoard());
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        assertFalse(game.won("X"));
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col);
        assertTrue(game.won("X"));

        // Diagonal L -> R and Down
        col = 0;
        Connect4.initializeBoard(game.getGameBoard());
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        Connect4.insertToken(game.getGameBoard(), "O", col);
        Connect4.insertToken(game.getGameBoard(), "X", col++);
        assertFalse(game.won("X"));
        Connect4.insertToken(game.getGameBoard(), "X", col);
        assertTrue(game.won("X"));
    }

    @Test
    void isADraw() {
        assertFalse(Connect4.isADraw(new String[6][7]));
        assertFalse(Connect4.isADraw(game.getGameBoard()));
        Connect4.initializeBoard(game.getGameBoard());
        assertFalse(Connect4.isADraw(game.getGameBoard()));
        for (int i = 0; i < game.getGameBoard().length; i++) {
            for (int j = 0; j < game.getGameBoard()[0].length; j++) {
                game.getGameBoard()[i][j] = "X";
            }
        }
        assertTrue(Connect4.isADraw(game.getGameBoard()));
    }

    @Test
    void makeMove() {
        // Players were never initialized
        assertNull(game.getPlayer1());
        assertNull(game.getPlayer2());
        assertNull(game.getCurrentPlayer());
        assertThrows(NullPointerException.class, () -> game.makeMove());
        Connect4.initializeBoard(game.getGameBoard());
        game.setPlayer1(new Connect4HumanPlayer("TestHumanPlayer", new Connect4GuiInterface.Token(true)));
        game.setPlayer2(new Connect4HumanPlayer("TestHumanPlayer", new Connect4GuiInterface.Token(true)));
        game.setCurrentPlayer(game.randomlySelectFirstPlayer());
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
        assertNotNull(game.getCurrentPlayer());
    }

    @Test
    void over() {
        assertFalse(game.over());
        game.setNumberOfMovesMade(Connect4.COLUMNS * Connect4.ROWS);
        assertTrue(game.over());
    }

    @Test
    void incrementNumberOfMovesMade() {
        assertEquals(0, game.getNumberOfMovesMade());
        game.incrementNumberOfMovesMade();
        assertEquals(1, game.getNumberOfMovesMade());
    }
}