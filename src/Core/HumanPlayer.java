package Core;

import java.util.Scanner;

import UI.Connect4GUI;

import static java.lang.System.out;

/**
 * Player serves as the base class for two opponents in a game of Connect 4.
 */
public class HumanPlayer implements Player {

    private final String marker;
    private final Scanner in;
    protected int numberOfMovesMade;
    private String name;
    private Connect4GUI.Token token;


    /**
     * @param name   of type String
     * @param marker of type String which represents which piece marker represents the player on the game board
     * @param in     Scanner object to retrieve user input with
     */
    public HumanPlayer(String name, String marker, Scanner in) {
        this.name = name;
        this.marker = marker;
        this.numberOfMovesMade = 0;
        this.in = in;
    }

    public HumanPlayer(String name, Connect4GUI.Token token) {
        this(name, (token.red) ? "X" : "O", null);
        this.token = token;
    }

    public HumanPlayer(Connect4GUI.Token token) {
        this("Default Name", token);
    }

    public Connect4GUI.Token getToken() {
        return token;
    }

    /**
     * Accessor
     *
     * @return Player name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator method
     *
     * @param name new name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Player marker accessor method
     *
     * @return Human Player marker
     */
    public String getMarker() {
        return this.marker;
    }

    /**
     * Increments the number of moves an individual player has made.
     */
    @Override
    public void incrementNumberOfMovesMade() {
        this.numberOfMovesMade++;
    }

    /**
     * Take the human player's input and then play their move selection
     *
     * @param originalGameBoard current game board state
     * @return return value is unused
     */
    @Override
    public int makeMove(String[][] originalGameBoard) {
        int numberOfMovesMadeLeft = 21 - this.numberOfMovesMade;
        try {
            Connect4.displayBoard(originalGameBoard);
            out.printf("%sPlayer: %s has %d moves left (Choose a column to place your marker (1 - 7)).%s",
                    CRLF, this.getName(), numberOfMovesMadeLeft, CRLF);

            while (!(Connect4.insertToken(originalGameBoard, this.getMarker(), in.nextInt() - 1))) {
                out.printf("%sToken could not be played. Please try again.%s", CRLF,
                        CRLF);
                Connect4.displayBoard(originalGameBoard);
                out.printf("%sPlayer: %s has %d moves left (Choose a column to place your marker in (1-7)).%s",
                        CRLF, this.getName(), numberOfMovesMadeLeft, CRLF);
            }
        } catch (Exception e) {
            out.println("Invalid column selection, please try again.");
            in.nextLine();
            this.makeMove(originalGameBoard);
        }

        return 0;
    }
}
