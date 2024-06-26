package org.crawler.console;

import static java.lang.String.format;
import static org.crawler.console.Color.Yellow;
import static org.crawler.console.ColorType.bright;
import static org.crawler.console.Colorizer.colorize;

public class Console {
    public static boolean colorizeLogs = true;

    /**
     * This method is used to get the caller's class name and line number.
     * @return A string containing the caller's class name and line number.
     */
    private static String getCallerInfo () {
        int traceOriginIndex = 3;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String name = stackTraceElements[traceOriginIndex].getClassName().split("\\.")[stackTraceElements[traceOriginIndex].getClassName().split("\\.").length - 1];
        return format("[%s]", name);
    }

    /**
     * This method is used to print a message to the console, including the caller's class name and line number.
     * @param message The message to be printed.
     */
    public static void print (String... message) {
        String caller = getCallerInfo();
        String merge = String.join(" ", message);

        System.out.println(colorize(caller + ":", Yellow, bright) + " " + merge);
    }

    /**
     * This method is used to print a message to the console, including the caller's class name and line number, with a specified color.
     * @param color The color that is applied to the message.
     * @param message The message to be printed.
     */
    public static void print (Color color, String... message) {
        String caller = getCallerInfo();
        String merge = String.join(" ", message);

        System.out.println(colorize(caller + ":", Yellow, bright) + " " + colorize(merge, color));
    }
}
