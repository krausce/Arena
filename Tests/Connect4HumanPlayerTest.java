import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import main.Core.Connect4;
import main.Core.Connect4HumanPlayer;
import main.UI.Connect4GuiInterface.Token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4HumanPlayerTest {
    private static final String MARKER = "X";
    private static final String NAME = "test";
    private static Connect4HumanPlayer testPlayer;

    private static final PrintStream systemOutputBackup = System.out;
    private static final PrintStream systemErrorOutputBackup = System.err;
    private static final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream testErrorOutput = new ByteArrayOutputStream();
    private static final InputStream systemInputBackup = System.in;

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

    @BeforeAll
    static void setup() {
        testPlayer = new Connect4HumanPlayer();
    }

    @Test
    void Connect4HumanPlayer() {
        Connect4HumanPlayer testHumanPlayer = new Connect4HumanPlayer(new Token(true));
        assertEquals("Default Name", testHumanPlayer.getName());
    }

    @Test
    void incrementNumberOfMovesMade() {
        int originalNumberOfMovesMade = testPlayer.getNumberOfMovesMade();
        testPlayer.incrementNumberOfMovesMade();
        assertEquals(originalNumberOfMovesMade + 1, testPlayer.getNumberOfMovesMade());
    }

    @Test
    void getToken() {
        assertNull(testPlayer.getToken());
        testPlayer.setToken(new Token(true));
        assertNotNull(testPlayer.getToken());
        assertTrue(testPlayer.getToken().red);
    }

    @Test
    void getName() {
        assertNull(testPlayer.getName());
        testPlayer.setName(NAME);
        assertEquals(NAME, testPlayer.getName());
    }

    @Test
    void setName() {
        testPlayer.setName(" ");
        assertEquals(" ", testPlayer.getName());
    }

    @Test
    void getMarker() {
        assertEquals(MARKER, testPlayer.getMarker());
    }

    @Test
    void makeMove() {
        ByteArrayInputStream testInput = new ByteArrayInputStream("a\n0\n8\n1".getBytes());
        System.setIn(testInput);
        Connect4HumanPlayer newPlayer = new Connect4HumanPlayer("TEST", "X", new Scanner(System.in));

        final String[][] testBoard = new String[6][7];
        Connect4.initializeBoard(testBoard);
        assertEquals(0, newPlayer.makeMove(testBoard));
        assertTrue(testOutput.toString().contains("Invalid column selection, please try again."));
        assertTrue(testOutput.toString().contains("Token could not be played. Please try again."));
    }
}