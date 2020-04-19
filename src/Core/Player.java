package Core;

import UI.Connect4GUI;

public interface Player {
    String CRLF = "\n\r";

    String getMarker();

    void incrementNumberOfMovesMade();

    Connect4GUI.Token getToken();

    String getName();

    void setName(String text);

    int makeMove(String[][] originalGameBoard);
}
