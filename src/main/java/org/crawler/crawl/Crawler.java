package org.crawler.crawl;

import org.crawler.Console.Console;
import org.crawler.adapters.JsoupAdapter;
import org.crawler.config.Configuration;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import static org.crawler.Console.Color.Red;


public class Crawler {
    private Configuration config;

    public Crawler (Configuration config) {
        this.config = config;
    }

    public Page crawl (String url) {
        Console.print("Crawler with id: " + Thread.currentThread().getId(), "is going to crawl", url, "\n");
        return recursiveCrawl(url, 0);
    }

    /**
     * @implNote Would be private, but is public for testing-purposes
     */
    public Page getPage (String url, int currentDepth) {
        try {
            //* In here to detect and store the reason for retrieval-failures (failureReason)
            Document document = JsoupAdapter.fetchDocument(url);

            String language = JsoupAdapter.getLanguage(document);
            Elements headings = JsoupAdapter.getHeadings(document);
            List<String> links = filterLinks(url, JsoupAdapter.getLinks(document));

            Page page = new Page(url, language, headings, links, currentDepth);
            Console.print("[id: " + Thread.currentThread().getId() + "]:", page.getPageLinks().size() + "", "Sublinks found in (depth", (currentDepth) + ")", url);

            return page;
        } catch (IOException e) {
            Console.print(Red, "Connection to", url, "failed.");
            return new Page(url, e.toString(), currentDepth);
        }
    }

    public Page recursiveCrawl (String url, int currentDepth) {
        if (currentDepth > this.config.getMaxDepth()) return null;
        if (!isValidUrl(url)) return null; //? Skips internal anchor-tags like /favicon

        Page page = getPage(url, currentDepth);

        for (String link : page.getPageLinks()) {
            Page subPage = recursiveCrawl(link, currentDepth + 1);

            //? (Exceeded Depth or Url Validity)
            if (subPage == null) continue;

            page.addSubPage(subPage);
        }

        return page;
    }

    private boolean isValidUrl (String url) {
        return url.startsWith("http://") || url.startsWith("https://");
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

    /**
     * Filters the links by removing duplicates, removing link-loops and checking whether domain was requested / allowed.
     * @param originUrl The origin link from which the links were extracted
     * @param links The links that should be filtered
     * @return The filtered and therefore cleaned links
     */
    private List<String> filterLinks (String originUrl, List<String> links) {
        links = removeDuplicateLinks(links);
        links = filterLinksByDomain(links);
        links = removeLinkLoops(originUrl, links);
        return links;
    }

    private List<String> removeDuplicateLinks (List<String> links) {
        return links.stream().distinct().toList();
    }

    private List<String> filterLinksByDomain (List<String> links) {
        return links.stream().filter(link -> isRequestedDomain(link, this.config.getDomains())).toList();
    }

    private List<String> removeLinkLoops (String origin, List<String> links) {
        return links.stream().filter(href -> !href.equals(origin)).toList();
    }
}