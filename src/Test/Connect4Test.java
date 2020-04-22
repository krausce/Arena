package Test;

import main.Core.Connect4;
import main.Core.HumanPlayer;
import main.UI.Connect4GuiInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4Test {

    private Connect4 game;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        game = new Connect4();
    }

    @org.junit.jupiter.api.Test
    void initializeBoard() {
        final String[][] testBoard = new String[6][7];
        assertNull(testBoard[0][0]);
        Connect4.initializeBoard(testBoard);
        assertEquals(" ", testBoard[0][0]);
    }

    @org.junit.jupiter.api.Test
    void insertToken() {
        Connect4.initializeBoard(game.getGameBoard());
        assertTrue(Connect4.insertToken(game.getGameBoard(), "X", 0));
        assertEquals("X", game.getGameBoard()[0][0]);

        assertFalse(Connect4.insertToken(game.getGameBoard(), "X", 8));
        assertFalse(Connect4.insertToken(game.getGameBoard(), "X", -1));
    }

    @org.junit.jupiter.api.Test
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

    @org.junit.jupiter.api.Test
    void isADraw() {
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

    @org.junit.jupiter.api.Test
    void makeMove() {
        // Players were never initialized
        assertNull(game.getPlayer1());
        assertNull(game.getPlayer2());
        assertNull(game.getCurrentPlayer());
        assertThrows(NullPointerException.class, () -> game.makeMove());
        Connect4.initializeBoard(game.getGameBoard());
        game.setPlayer1(new HumanPlayer("TestHumanPlayer", new Connect4GuiInterface.Token(true)));
        game.setPlayer2(new HumanPlayer("TestHumanPlayer", new Connect4GuiInterface.Token(true)));
        game.setCurrentPlayer(game.randomlySelectFirstPlayer());
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
        assertNotNull(game.getCurrentPlayer());
    }

    @org.junit.jupiter.api.Test
    void over() {
        assertFalse(game.over());
        game.setNumberOfMovesMade(Connect4.COLUMNS * Connect4.ROWS);
        assertTrue(game.over());
    }

    @org.junit.jupiter.api.Test
    void incrementNumberOfMovesMade() {
        assertEquals(0, game.getNumberOfMovesMade());
        game.incrementNumberOfMovesMade();
        assertEquals(1, game.getNumberOfMovesMade());
    }
}