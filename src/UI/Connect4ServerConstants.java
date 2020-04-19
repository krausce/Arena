package UI;

import Core.Connect4;

public abstract class Connect4ServerConstants {
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int PLAYER_1_WON = 3;
    public static final int PLAYER_2_WON = 4;
    public static final int DRAW = 5;
    public static final int CONTINUE = 6;
    public static final String HOST = "localhost";
    public static final int LOCALHOST = 8004;
    public static final int ROWS = Connect4.ROWS;
    public static final int COLUMNS = Connect4.COLUMNS;
    public static final String PLAYER_1_TOKEN = "X";
    public static final String PLAYER_2_TOKEN = "O";

    private Connect4ServerConstants() {
    }

}
