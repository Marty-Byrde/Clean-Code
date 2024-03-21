package org.crawler.crawl;

import org.crawler.config.Configuration;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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


    private ArrayList<String> getFilteredPageLinks (Document document, String[] allowedDomains) {
        Elements anchors = document.select("a[href]");
        removeWrongDomainLinks(anchors, allowedDomains);
        return removeDuplicateLinks(anchors);
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


    private ArrayList<String> removeDuplicateLinks (Elements anchors) {
        ArrayList<String> links = new ArrayList<>();
        for (Element anchor : anchors) {
            String href = anchor.attr("abs:href");
            if (!links.contains(href)) links.add(href);
        }

        return links;
    }

    private void removeWrongDomainLinks (Elements anchors, String[] allowedDomains) {
        anchors.filter((anchor, index) -> {
            String href = anchor.attr("abs:href");

            if (href.isEmpty()) return NodeFilter.FilterResult.SKIP_ENTIRELY;

            // Skipping non-relevant links.
            if (!isRequestedDomain(href, allowedDomains)) {
                return NodeFilter.FilterResult.SKIP_ENTIRELY;
            }

            return NodeFilter.FilterResult.CONTINUE;
        });
    }

    private List<String> removeLinkLoops (String origin, List<String> links) {
        return links.stream().filter(href -> !href.equals(origin)).toList();
    }
}