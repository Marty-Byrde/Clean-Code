package org.crawler;

import org.jsoup.nodes.Element;

public class Printer {


    private static int getHeadingLevel (Element heading) {
        if (!heading.tagName().startsWith("h") && heading.tagName().length() != 2) return -1;

        return Integer.parseInt(heading.tagName().split("h")[1]);
    }
    private static String createHeading(int level, String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("#".repeat(level));
        builder.append(" ");
        builder.append(title);

        return builder.toString();
    }

}
