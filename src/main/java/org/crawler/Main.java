package org.crawler;

import org.crawler.config.Configuration;
import org.crawler.console.Color;
import org.crawler.console.Console;
import org.crawler.crawl.ConcurrencyHandler;
import org.crawler.crawl.Page;

import java.util.List;

public class Main {
    public static void main (String[] args) {
        if (!System.getenv().containsKey("API_KEY")) {
            System.out.println("Translation API-Key not found. Please set the API-Key as an environment variable.");
            return;
        }

        Configuration config = Configuration.requestConfiguration();

        ConcurrencyHandler handler = new ConcurrencyHandler(config);
        List<Page> thread_results = handler.getResults();

        System.out.println();
        Console.print(Color.Green, "All Crawlers have finished, for these urls:");
        thread_results.forEach(result -> System.out.println(result.getUrl()));

        Printer.printReports(thread_results);
    }
}
