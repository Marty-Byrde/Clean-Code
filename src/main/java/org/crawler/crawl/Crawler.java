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

    /**
     * Filters out the links that are not from the requested domain.
     * @return In case the link is from the requested domain, then true is returned, otherwise false. If no domains are specified, then the link's domain is accepted automatically.
     */
    private boolean isRequestedDomain (String link, String[] domains) {
        for (String domain : domains) {
            if (!link.contains(domain)) return false;
        }
        return true;
    }

}