package org.crawler;

import org.crawler.config.Configuration;
import org.crawler.crawl.Crawler;
import org.crawler.crawl.PageInfo;

import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException {
        if (!System.getenv().containsKey("API_KEY")) {
            System.out.println("Translation API-Key not found. Please set the API-Key as an environment variable.");
            return;
        }

        Configuration config = Configuration.requestConfiguration();
        Crawler crawler = new Crawler(config);
        PageInfo result = crawler.crawl();
        Printer.printReport(Printer.createReport(result));

    }
}
