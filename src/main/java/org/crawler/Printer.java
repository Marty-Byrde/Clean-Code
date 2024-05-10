package org.crawler;

import org.crawler.Console.Console;
import org.crawler.crawl.PageInfo;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.crawler.Console.Color.Red;

public class Printer {
    public static void printReport (List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("./report.md"));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

    public static List<String> createReport (PageInfo page) {
        List<String> lines = new ArrayList<>();

        lines.add(String.format("input: <%s>", page.getUrl()));
        lines.add(String.format("language: <%s>", page.getLanguage()));
        if (page.isBroken()) lines.add("Broken page!");

        lines.addAll(formatPageHeadings(page));
        lines.addAll(createSubPagesReport(page));

        return lines;
    }

    /**
     * This method creates a report for multiple PageInfos.
     * @param pageInfos A list of PageInfos for which a report shall be created.
     */
    public static void printReports (List<PageInfo> pageInfos) {
        List<String> reportLines = new ArrayList<>();

        for (PageInfo pageInfo : pageInfos) {
            reportLines.addAll(createReport(pageInfo));

            String[] spacings = new String[]{"", "", "", "", ""};
            reportLines.addAll(Arrays.stream(spacings).toList());
        }

        try {
            printReport(reportLines);
        } catch (IOException e) {
            Console.print(Red, "Creating single report for multiple PageInfos failed.");
        }
    }

    private static List<String> formatPageHeadings (PageInfo page) {
        List<String> headings = new ArrayList<>();

        for (Element heading : page.getHeadings()) {
            String arrow = ("-").repeat(page.getDepth());
            if (page.getDepth() > 0) arrow += "> ";

            int level = getHeadingLevel(heading);
            headings.add(createHeading(level, arrow + heading.text()));
        }

        return headings;
    }

    private static List<String> createSubPagesReport (PageInfo page) {
        List<String> lines = new ArrayList<>();

        for (PageInfo subPage : page.getSubPagesInfo()) {
            lines.add("");
            lines.add("");
            lines.addAll(createReport(subPage));
        }

        return lines;
    }

    private static int getHeadingLevel (Element heading) {
        if (!heading.tagName().startsWith("h") && heading.tagName().length() != 2) return -1;

        return Integer.parseInt(heading.tagName().split("h")[1]);
    }

    private static String createHeading (int level, String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("#".repeat(level));
        builder.append(" ");
        builder.append(title);

        return builder.toString();
    }

}
