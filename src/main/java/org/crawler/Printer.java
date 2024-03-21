package org.crawler;

import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Printer {
    public static void printReport (List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("./report.md"));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

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
