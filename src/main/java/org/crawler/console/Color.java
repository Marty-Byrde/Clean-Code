package org.crawler.console;

/**
 * Enum class that offers a variety of colors for the console output.
 * Each color has then different variants like bold, underlined, bright, etc. that can be accessed using the enum's getters.
 */
public enum Color {
    Black("\033[0;30m", "\033[1;30m", "\033[4;30m", "\033[40m", "\033[0;90m", "\033[1;90m", "\033[0;100m"),
    Red("\033[0;31m", "\033[1;31m", "\033[4;31m", "\033[41m", "\033[0;91m", "\033[1;91m", "\033[0;101m"),
    Green("\033[0;32m", "\033[1;32m", "\033[4;32m", "\033[42m", "\033[0;92m", "\033[1;92m", "\033[0;102m"),
    Yellow("\033[0;33m", "\033[1;33m", "\033[4;33m", "\033[43m", "\033[0;93m", "\033[1;93m", "\033[0;103m"),
    Blue("\033[0;34m", "\033[1;34m", "\033[4;34m", "\033[44m", "\033[0;94m", "\033[1;94m", "\033[0;104m"),
    Purple("\033[0;35m", "\033[1;35m", "\033[4;35m", "\033[45m", "\033[0;95m", "\033[1;95m", "\033[0;105m"),
    Cyan("\033[0;36m", "\033[1;36m", "\033[4;36m", "\033[46m", "\033[0;96m", "\033[1;96m", "\033[0;106m"),
    White("\033[0;37m", "\033[1;37m", "\033[4;37m", "\033[47m", "\033[0;97m", "\033[1;97m", "\033[0;107m");

    private final String regular;
    private final String bold;
    private final String underlined;
    private final String background;
    private final String bright;
    private final String boldBright;
    private final String backgroundBright;

    Color (String regular, String bold, String underlined, String background,
           String bright, String boldBright, String backgroundBright) {
        this.regular = regular;
        this.bold = bold;
        this.underlined = underlined;
        this.background = background;
        this.bright = bright;
        this.boldBright = boldBright;
        this.backgroundBright = backgroundBright;
    }

    public String getRegular () {
        return regular;
    }

    public String getBold () {
        return bold;
    }

    public String getUnderlined () {
        return underlined;
    }

    public String getBackground () {
        return background;
    }

    public String getBright () {
        return bright;
    }

    public String getBoldBright () {
        return boldBright;
    }

    public String getBackgroundBright () {
        return backgroundBright;
    }
}