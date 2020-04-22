import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.Core.Connect4ComputerPlayer;
import main.Core.GameBoard;
import main.UI.Connect4GuiInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Connect4ComputerPlayerTest {
    private static Connect4ComputerPlayer player;

    @BeforeAll
    static void setup() {
        player = new Connect4ComputerPlayer(new Connect4GuiInterface.Token(false), "O");
    }

    @AfterAll
    static void tearDown() {
        player = null;
    }

    @Test
    void setStaticBestMoveColumn() {
        assertEquals(-1, Connect4ComputerPlayer.getStaticBestMoveColumn());
        Connect4ComputerPlayer.setStaticBestMoveColumn(3);
        assertEquals(3, Connect4ComputerPlayer.getStaticBestMoveColumn());
    }

    @Test
    void setMaxDepth() {
        assertEquals(4, Connect4ComputerPlayer.getMaxDepth());
        Connect4ComputerPlayer.setMaxDepth(6);
        assertEquals(6, Connect4ComputerPlayer.getMaxDepth());
    }

    @Test
    void setStaticstaticBestMoveScore() {
        assertEquals(0.0, Connect4ComputerPlayer.getBestMoveScore());
        Connect4ComputerPlayer.setStaticstaticBestMoveScore(1.0);
        assertEquals(1.0, Connect4ComputerPlayer.getBestMoveScore());
    }

    @Test
    void makeMove() {
        assertThrows(IllegalStateException.class, () -> player.makeMove(new String[6][7]));
        GameBoard.initializeBoard(GameBoard.connect4Board);
        assertEquals(Connect4ComputerPlayer.getStaticBestMoveColumn(), player.makeMove(GameBoard.connect4Board));
    }

    @Test
    void getMarker() {
        assertEquals("O", player.getMarker());
    }

    @Test
    void getToken() {
        assertNotNull(player.getToken());
        assertFalse(player.getToken().red);
    }

    @Test
    void getRobotName() {
        assertNotNull(player.getName());
        assertEquals(Connect4ComputerPlayer.ROBOT_NAME, player.getName());
    }

    @Test
    void setName() {
        player.setName("Test Robot");
        assertEquals(Connect4ComputerPlayer.ROBOT_NAME, player.getName());
    }
}