package org.crawler.adapters;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsoupAdapter {
    public static Document fetchDocument (String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        return connection.get();
    }

    public static String getLanguage (Document document) {
        return document.select("html").attr("lang");
    }

    public static Elements getHeadings (Document document) {
        return document.select("h1, h2, h3, h4, h5, h6");
    }

    public static List<String> getLinks (Document document) {
        Elements anchors = document.select("a");
        List<String> links = new ArrayList<>();

        anchors.forEach(element -> links.add(element.attr("href")));
        return links;
    }
}
