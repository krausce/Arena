package main.Core;

import main.UI.Connect4GuiInterface;

public interface Player {
    String CRLF = "\n\r";

    String getMarker();

    void incrementNumberOfMovesMade();

    Connect4GuiInterface.Token getToken();

    String getName();

    void setName(String text);

    int makeMove(String[][] originalGameBoard);
}
