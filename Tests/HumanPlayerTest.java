import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.Core.Connect4;
import main.Core.HumanPlayer;
import main.UI.Connect4GuiInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HumanPlayerTest {
    private static final String MARKER = "X";
    private static final String NAME = "test";
    private static HumanPlayer testPlayer;

    @BeforeAll
    static void setup() {
        testPlayer = new HumanPlayer();
    }

    @Test
    void getToken() {
        assertNull(testPlayer.getToken());
        testPlayer.setToken(new Connect4GuiInterface.Token(true));
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
        final String[][] testBoard = new String[6][7];
        Connect4.initializeBoard(testBoard);
        assertTrue(HumanPlayer.makeMove(testBoard, testPlayer.getMarker(), 0));
    }
}