import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import main.Core.Connect4;
import main.Core.Connect4ComputerPlayer;
import main.Core.GameBoard;
import main.UI.Connect4GuiInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4ComputerPlayerTest {
    private static Connect4ComputerPlayer player;
    private String[][] testBoard;

    @BeforeEach
    void init() {
        testBoard = new String[6][7];
        Connect4.initializeBoard(testBoard);
    }

    @BeforeAll
    static void setup() {
        player = new Connect4ComputerPlayer(new Connect4GuiInterface.Token(false), "O");
    }

    @AfterAll
    static void tearDown() {
        player = null;
    }

    @Test
    void Connect4ComputerPlayer() {
        Connect4ComputerPlayer testPlayer = new Connect4ComputerPlayer(new Connect4GuiInterface.Token(false), "O", 4);
        assertNotNull(testPlayer);
        testPlayer = new Connect4ComputerPlayer("O", 4);
        assertNotNull(testPlayer);
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
    void setStaticBestMoveScore() {
        assertEquals(0.0, Connect4ComputerPlayer.getBestMoveScore());
        Connect4ComputerPlayer.setStaticBestMoveScore(1.0);
        assertEquals(1.0, Connect4ComputerPlayer.getBestMoveScore());
    }

    @Test
    void makeMove() {
        assertThrows(IllegalStateException.class, () -> player.makeMove(new String[6][7]));
        GameBoard.initializeBoard(GameBoard.connect4Board);
        assertEquals(Connect4ComputerPlayer.getStaticBestMoveColumn(), player.makeMove(GameBoard.connect4Board));
        for (int i = 0; i < testBoard.length - 3; i++) {
            testBoard[i][0] = "X";
        }
        assertEquals(0, player.makeMove(testBoard));
        Connect4.initializeBoard(testBoard);
        for (int i = 0; i < testBoard.length - 3; i++) {
            testBoard[i][0] = "O";
        }
        assertEquals(0, player.makeMove(testBoard));
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

    @Test
    void initializeColumnSelectionOrderArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testInitializeColumnSelectionOrderArrayMethod = Connect4ComputerPlayer.class.getDeclaredMethod(
                "initializeColumnSelectionOrderArray", int.class);
        testInitializeColumnSelectionOrderArrayMethod.setAccessible(true);
        int[] testIntArray = (int[]) testInitializeColumnSelectionOrderArrayMethod.invoke(player, 7);
        assertNotNull(testIntArray);
        assertEquals(7, testIntArray.length);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAvailableMoves() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < testBoard.length; i++) {
            testBoard[i][0] = "X";
        }
        Method testGetAvailableMovesMethod = Connect4ComputerPlayer.class.getDeclaredMethod("getAvailableMoves", String[][].class);
        testGetAvailableMovesMethod.setAccessible(true);
        ArrayList<Integer> testArrayList = (ArrayList<Integer>) testGetAvailableMovesMethod.invoke(player, (Object) testBoard);
        assertEquals(6, testArrayList.size());
    }

    @Test
    void otherMarker() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testOtherMarkerMethod = Connect4ComputerPlayer.class.getDeclaredMethod("otherMarker", String.class);
        testOtherMarkerMethod.setAccessible(true);
        assertEquals("X", testOtherMarkerMethod.invoke(player, "O"));
    }

    @Test
    void isWinningMove() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < testBoard.length - 3; i++) {
            testBoard[i][0] = "X";
        }
        Method testIsWinningMoveMethod = Connect4ComputerPlayer.class.getDeclaredMethod("isWinningMove", String[][].class, String.class,
                int.class);
        testIsWinningMoveMethod.setAccessible(true);
        assertTrue((boolean) testIsWinningMoveMethod.invoke(player, testBoard, "X", 0));
        assertFalse((boolean) testIsWinningMoveMethod.invoke(player, testBoard, "X", 1));
        assertFalse((boolean) testIsWinningMoveMethod.invoke(player, testBoard, "X", 8));
        assertFalse((boolean) testIsWinningMoveMethod.invoke(player, testBoard, "X", -1));
    }

    @Test
    void undoMove() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testUndoMoveMethod = Connect4ComputerPlayer.class.getDeclaredMethod("undoMove", String[][].class, int.class);
        testUndoMoveMethod.setAccessible(true);
        Connect4.insertToken(testBoard, "X", 0);
        assertNotEquals(" ", testBoard[0][0]);
        testUndoMoveMethod.invoke(player, testBoard, 0);
        assertEquals(" ", testBoard[0][0]);
    }

    @Test
    void incrementNumberOfMovesMade() {
        int initialMovesMade = player.getNumberOfMovesMade();
        player.incrementNumberOfMovesMade();
        assertEquals(initialMovesMade + 1, player.getNumberOfMovesMade());
    }
}