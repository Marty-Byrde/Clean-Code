package org.crawler.config;

import org.crawler.Console.Colorizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.crawler.Console.Color.Cyan;
import static org.crawler.Console.Color.Green;
import static org.crawler.Console.ColorType.bold;
import static org.crawler.config.InputValidation.validate;

public class Configuration {

    private String[] urls;
    private int maxDepth;
    private String[] domains;

    //! not used due to the api-limitation of 1000 requests per month.
    @SuppressWarnings("FieldCanBeLocal")
    private String targetLanguage;

    public Configuration (String[] urls, int maxDepth, String[] domains, String targetLanguage) {
        this.urls = urls;
        this.maxDepth = maxDepth;
        this.domains = domains;
        this.targetLanguage = targetLanguage;
    }

    public static Configuration requestConfiguration () {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println(Colorizer.colorize("Welcome to the Crawler-Configuration-Tool!", Green, bold));

            System.out.println("Separate multiple domains with a comma (,), e.g. 'google.com, orf.at' or provide a single or e.g. 'google.com'");
            System.out.print(Colorizer.colorize("Please enter the URL(s) you want to crawl: ", Cyan));
            String[] urls = validate(reader.readLine(), "https://www.orf.at/", "(Default): No value has been entered. Default: 'https://www.orf.at/'").split(",");


            System.out.print(Colorizer.colorize("Please enter the maximum depth you want to crawl (max. 2):", Cyan));
            int maxDepth = Math.max(validate(reader.readLine(), 1, "(Default): No value has been entered. Default: 1"), 2);


            System.out.println();
            System.out.println("Separate multiple domains with a comma (,)");
            System.out.println("To allow all domains, leave the field empty");
            System.out.println("Example: google.com, orf.at");
            System.out.print(Colorizer.colorize("Please enter the domains you want to crawl: ", Cyan));
            String domainInput = validate(reader.readLine(), "", "(Default): No value has been entered. Default: All domains");
            String[] domains = domainInput.trim().split(",");


            System.out.print(Colorizer.colorize("Please enter the language you want to crawl (Example: en, de): ", Cyan));
            String language = validate(reader.readLine(), "en", "(Default): No value has been entered. Default: en");


            return new Configuration(urls, maxDepth, domains, language);
        } catch (IOException e) {
            System.out.println("Error while reading input. Please try again.");
            return requestConfiguration();
        }
    }


    public String[] getUrls () {
        return urls;
    }

    public int getMaxDepth () {
        return maxDepth;
    }

    public String[] getDomains () {
        return domains;
    }
}
