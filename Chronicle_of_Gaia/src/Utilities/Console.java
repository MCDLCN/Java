package Utilities;

public class Console {

    public enum ConsoleColor {

        // Reset
        RESET("\u001B[0m"),

        // Styles
        BOLD("\u001B[1m"),
        DIM("\u001B[2m"),
        ITALIC("\u001B[3m"),
        UNDERLINE("\u001B[4m"),
        BLINK("\u001B[5m"),
        REVERSE("\u001B[7m"),
        STRIKETHROUGH("\u001B[9m"),

        // Regular colors
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),
        BROWN(Console.fg256(94)),
        PINK(Console.fg256(205)),
        ORANGE(Console.fg256(208)),
        GOLD(Console.fg256(220)),
        TEAL(Console.fg256(37)),

        // Bright colors
        BRIGHT_BLACK("\u001B[90m"),
        BRIGHT_RED("\u001B[91m"),
        BRIGHT_GREEN("\u001B[92m"),
        BRIGHT_YELLOW("\u001B[93m"),
        BRIGHT_BLUE("\u001B[94m"),
        BRIGHT_PURPLE("\u001B[95m"),
        BRIGHT_CYAN("\u001B[96m"),
        BRIGHT_WHITE("\u001B[97m"),

        // Background colors
        BG_BLACK("\u001B[40m"),
        BG_RED("\u001B[41m"),
        BG_GREEN("\u001B[42m"),
        BG_YELLOW("\u001B[43m"),
        BG_BLUE("\u001B[44m"),
        BG_PURPLE("\u001B[45m"),
        BG_CYAN("\u001B[46m"),
        BG_WHITE("\u001B[47m"),

        // Bright background colors
        BG_BRIGHT_BLACK("\u001B[100m"),
        BG_BRIGHT_RED("\u001B[101m"),
        BG_BRIGHT_GREEN("\u001B[102m"),
        BG_BRIGHT_YELLOW("\u001B[103m"),
        BG_BRIGHT_BLUE("\u001B[104m"),
        BG_BRIGHT_PURPLE("\u001B[105m"),
        BG_BRIGHT_CYAN("\u001B[106m"),
        BG_BRIGHT_WHITE("\u001B[107m");

        private final String code;

        ConsoleColor(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static final String RESET = "\u001B[0m";

    public static void print(String text, ConsoleColor color) {
        System.out.println(color.getCode() + text + ConsoleColor.RESET.getCode());
    }

    public static void print(String text) {
        print(text,ConsoleColor.BRIGHT_CYAN);
    }

    public static String fg256(int n) { return "\u001B[38;5;" + n + "m"; }
    public static String bg256(int n) { return "\u001B[48;5;" + n + "m"; }
}