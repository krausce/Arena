package main.Core;

import static java.lang.System.out;

@SuppressWarnings("StringBufferReplaceableByString")
public final class Menus {

    private static final StringBuilder menu = new StringBuilder();
    private static final String LINE = "          ************************************************************\n";
    private static final String BLANK_LINE = "          *                                                          *\n";

    private Menus() {
    }

    public static void displayMessage(String message) {
        out.println(message);
    }

    public static void displayStartMenu() {
        StringBuilder startMenu = new StringBuilder();
        startMenu.append(LINE);
        startMenu.append(BLANK_LINE);
        startMenu.append("          *          Select one of the following game modes          *\n");
        startMenu.append("          *              1.) Console Game (Current Mode)             *\n");
        startMenu.append("          *              2.) GUI Mode (On Screen Display)            *\n");
        startMenu.append(BLANK_LINE);
        startMenu.append(LINE);
        out.println(startMenu.toString());
        menu.setLength(0);
    }

    public static void displayPlayerModeMenu() {
        menu.append(LINE);
        menu.append(BLANK_LINE);
        menu.append("          *          Select one of the following game modes          *\n");
        menu.append("          *                   1.) HUMAN vs. HUMAN                    *\n");
        menu.append("          *                   2.) HUMAN vs. ROBOT                    *\n");
        menu.append(BLANK_LINE);
        menu.append(LINE);
        out.println(menu.toString());
        menu.setLength(0);
    }

    public static void displayMaxDepthSelectionMenu() {
        menu.append(LINE);
        menu.append(BLANK_LINE);
        menu.append("          *                Select difficulty level:                  *\n");
        menu.append("          *                  1. Normal   2. Hard                     *\n");
        menu.append(BLANK_LINE);
        menu.append(LINE);
        out.println(menu);
        menu.setLength(0);
    }
}
