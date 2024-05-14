package org.crawler.crawl;

import org.crawler.Console.Console;
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

import static org.crawler.Console.Color.Red;


public class Crawler {
    private Configuration config;

    public Crawler (Configuration config) {
        this.config = config;
    }

    public Page crawl (String url) {
        Console.print("Crawler with id: " + Thread.currentThread().getId(), "is going to crawl", url, "\n");
        return getPage(url, 0);
    }

    private Page getPage (String url, int currentDepth) {
        if (currentDepth > this.config.getMaxDepth()) return null;
        Page page = retrievePageInfo(url, currentDepth);
        Console.print("[id: " + Thread.currentThread().getId() + "]:", page.getPageLinks().size() + "", "Sublinks found in (depth", (currentDepth) + ")", url);

        recursion(page, currentDepth);
        return page;
    }

    /**
     * @implNote Would be private if it wasn't for testing purposes.
     */
    public Page retrievePageInfo (String url, int depth) {
        Page result = new Page(url, "", new Elements(), new ArrayList<>(), depth);
        Document document = null;
        try {
            document = fetchDocument(url);
        } catch (IOException e) {
            Console.print(Red, "Document Retrieval for", url, "failed.");

            //* In case one cannot connect to the requested URL
            result.setFailureReason(e.toString());
            return result;
        }

        result.setLanguage(getSourceLanguage(document));
        result.setHeadings(document.select("h1, h2, h3, h4, h5, h6"));
        result.setPageLinks(removeLinkLoops(url, getFilteredPageLinks(document, this.config.getDomains())));

        return result;
    }

    private void recursion (Page originPage, int currentDepth) {
        ArrayList<Page> results = new ArrayList<>();
        for (String link : originPage.getPageLinks()) {
            Page recursiveResult = getPage(link, currentDepth + 1);
            if (recursiveResult != null) results.add(recursiveResult);

            if (results.size() == 10) break;
        }

        originPage.setSubPagesInfo(results);
    }

    /**
     * @implNote Would be private if it wasn't for testing purposes.
     */
    public Document fetchDocument (String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        return connection.get();
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