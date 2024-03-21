package org.crawler.crawl;

import org.crawler.config.Configuration;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class Crawler {
    private Configuration config;

    public Crawler (Configuration config) {
        this.config = config;
    }


    /**
     * @implNote Would be private if it wasn't for testing purposes.
     */
    public Document getDocument (String url) {
        try {
            Connection connection = Jsoup.connect(url);
            return connection.get();
        } catch (IOException e) {
            System.out.println("Error while connecting to the URL: " + url);
        } catch (Exception ignored) {
            System.out.println("Please provide a valid URL");
        }
        return null;
    }


    private String getSourceLanguage (Document document) {
        String sourceLanguageISO = document.getElementsByTag("html").attr("lang");
        return sourceLanguageISO.split("-")[0];
    }
}