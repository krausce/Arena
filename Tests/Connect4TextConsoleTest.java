import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import main.UI.Connect4TextConsole;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4TextConsoleTest {

    private static final PrintStream systemOutputBackup = System.out;
    private static final PrintStream systemErrorOutputBackup = System.err;
    private static final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream testErrorOutput = new ByteArrayOutputStream();
    private static final InputStream systemInputBackup = System.in;
    private static Scanner in;

    @BeforeAll
    static void initializeScanner() {
        System.setOut(new PrintStream(testOutput));
        System.setErr(new PrintStream(testErrorOutput));
    }

    @AfterAll
    static void resetInputOutput() {
        System.setOut(systemOutputBackup);
        System.setErr(systemErrorOutputBackup);
        System.setIn(systemInputBackup);
    }

    @Test
    void getGameMode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ByteArrayInputStream testInput = new ByteArrayInputStream("a\n0\n3\n1".getBytes());
        System.setIn(testInput);
        in = new Scanner(System.in);

        Method testGetGameModeMethod = Connect4TextConsole.class.getDeclaredMethod("getGameMode", Scanner.class);
        testGetGameModeMethod.setAccessible(true);

        assertFalse((Boolean) testGetGameModeMethod.invoke(Connect4TextConsole.class, in));
        assertTrue(testOutput.toString().contains("Invalid entry, please try again."));
        assertTrue(testOutput.toString().contains("Invalid game mode entry, please try again."));
    }

    @Test
    void main() {
        ByteArrayInputStream testInput;
        StringBuilder userInputBuilder = new StringBuilder();
        userInputBuilder.append("1\n1\nplayer1\nplayer2")
                .append("\n1\n2") // Moves for both Human Players... vertical win test
                .append("\n1\n2")
                .append("\n1\n2")
                .append("\n1");
        testInput = new ByteArrayInputStream(userInputBuilder.toString().getBytes());
        final ByteArrayInputStream finalTestInput = testInput;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(finalTestInput);
                Connect4TextConsole.main(new String[]{});
                assertTrue(testOutput.toString().contains("Congratulations"));
            }
        }).start();

        userInputBuilder.setLength(0);
        userInputBuilder.append("1\n1\nplayer1\nplayer2")
                .append("\n1\n1") // Moves for both Human Players... horizontal win test
                .append("\n2\n2")
                .append("\n3\n3")
                .append("\n4");
        testInput = new ByteArrayInputStream(userInputBuilder.toString().getBytes());
        final ByteArrayInputStream finalTestInput2 = testInput;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(finalTestInput2);
                Connect4TextConsole.main(new String[]{});
                assertTrue(testOutput.toString().contains("Congratulations"));
            }
        }).start();

        userInputBuilder.setLength(0);
        userInputBuilder.append("1\n1\nplayer1\nplayer2")
                .append("\n1\n2")
                .append("\n2\n3")
                .append("\n3\n4")
                .append("\n3\n4")
                .append("\n4\n5\n4"); // L - R and Up diagonal win test
        testInput = new ByteArrayInputStream(userInputBuilder.toString().getBytes());
        final ByteArrayInputStream finalTestInput3 = testInput;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(finalTestInput3);
                Connect4TextConsole.main(new String[]{});
                assertTrue(testOutput.toString().contains("Congratulations"));
            }
        }).start();

        userInputBuilder.setLength(0);
        userInputBuilder.append("1\n1\nplayer1\nplayer2")
                .append("\n1\n1\n1\n1")
                .append("\n2\n2\n3\n2")
                .append("\n6\n3\n6\n4"); // L - R and Down diagonal win test
        testInput = new ByteArrayInputStream(userInputBuilder.toString().getBytes());
        final ByteArrayInputStream finalTestInput4 = testInput;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(finalTestInput4);
                Connect4TextConsole.main(new String[]{});
                assertTrue(testOutput.toString().contains("Congratulations"));
            }
        }).start();

        userInputBuilder.setLength(0);
        userInputBuilder.append("1\n1\nplayer1\nplayer2")
                .append("\n1\n2\n2\n1\n1\n2\n2\n1\n1\n2\n2\n1")
                .append("\n4\n3\n3\n4\n4\n3\n3\n4\n4\n3\n3\n4")
                .append("\n5\n6\n6\n5\n5\n6\n6\n5\n5\n6\n6\n5")
                .append("\n7\n7\n7\n7\n7\n7"); // Test for a draw
        testInput = new ByteArrayInputStream(userInputBuilder.toString().getBytes());
        final ByteArrayInputStream finalTestInput5 = testInput;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(finalTestInput5);
                Connect4TextConsole.main(new String[]{});
                assertTrue(testOutput.toString().contains("Game Over:"));
            }
        }).start();
    }
}