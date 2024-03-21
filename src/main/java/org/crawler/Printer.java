package org.crawler;

public class Printer {

    private static String createHeading(int level, String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("#".repeat(level));
        builder.append(" ");
        builder.append(title);

        return builder.toString();
    }

}
