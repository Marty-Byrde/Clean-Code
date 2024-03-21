package org.crawler.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Configuration {

    private String url;
    private int maxDepth;
    private String[] domains;
    private String targetLanguage;

    public Configuration (String url, int maxDepth, String[] domains, String targetLanguage) {
        this.url = url;
        this.maxDepth = maxDepth;
        this.domains = domains;
        this.targetLanguage = targetLanguage;
    }

    public static Configuration requestConfiguration () {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Please enter the URL you want to crawl: ");
            String url = reader.readLine();

            System.out.println("Please enter the maximum depth you want to crawl (max. 2): ");
            int maxDepth = Math.max(Integer.parseInt(reader.readLine()), 2);

            System.out.println("Please enter the domains you want to crawl: ");
            System.out.println("Separate multiple domains with a comma (,)");
            System.out.println("To allow all domains, leave the field empty");
            System.out.println("Example: google.com, orf.at");

            String[] domains = reader.readLine().trim().split(",");

            System.out.println("Please enter the language you want to crawl: ");
            System.out.println("Example: en, de");
            String language = reader.readLine();


            return new Configuration(url, maxDepth, domains, language);
        } catch (IOException e) {
            System.out.println("Error while reading input. Please try again.");
            return requestConfiguration();
        }
    }


    public String getUrl () {
        return url;
    }

    public int getMaxDepth () {
        return maxDepth;
    }

    public String[] getDomains () {
        return domains;
    }
}
