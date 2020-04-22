package main.UI;

import java.util.InputMismatchException;
import java.util.Scanner;

import main.Core.Connect4;
import main.Core.Menus;

import static java.lang.System.exit;
import static java.lang.System.out;

/**
 * Connect 4 is a console game played by two players.
 */
public class Connect4TextConsole {

    public static final String CRLF = "\n\r";
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length == 0) {
            if (getGameMode(in)) {
                exit(0);
            }
        }

        boolean won;
        Connect4 game = null;
        try {
            game = new Connect4(in);
        } catch (InputMismatchException | IllegalArgumentException e) {
            out.println("Invalid game option selection. Exiting...");
            exit(-1);
        }
        do {
            game.nextPlayer();
            game.makeMove();
            game.getCurrentPlayer().incrementNumberOfMovesMade();
            game.incrementNumberOfMovesMade();
            won = game.won(game.getCurrentPlayer().getMarker());
        } while (!game.over() && !won);

        Connect4.displayBoard(game.getGameBoard());
        if (won) {
            out.printf("%sCongratulations %s! You've won the game!%s", CRLF, game.getCurrentPlayer().getName(), CRLF);
        } else {
            out.printf("%sGame Over: Players %s and %s ended the game with a draw.%s", CRLF,
                    "Player X", "Player O", CRLF);
        }
    }

    private static boolean getGameMode(Scanner in) {
        int gameMode = 0;
        do {
            Menus.displayStartMenu();
            if (in.hasNextInt()) {
                gameMode = in.nextInt();
            } else {
                Menus.displayMessage("Invalide entry, please try again.");
                continue;
            }
            if (gameMode < 1 || gameMode > 2) {
                Menus.displayMessage("Invalid game mode entry, please try again.");
            }
        } while (gameMode < 1 || gameMode > 2);

        if (gameMode == 2) {
            Connect4GUI.main(new String[]{Integer.toString(2)});
            return true;
        }

        return false;
    }
}
