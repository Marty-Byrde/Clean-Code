package org.crawler.crawl;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class Page {
    private String url;
    private String language = "";
    private Elements headings = new Elements();
    private List<String> pageLinks = new ArrayList<>();
    private ArrayList<Page> subPagesInfo = new ArrayList<>();
    private int depth;
    private String failureReson;

    public Page (String url, String language, Elements headings, List<String> pageLinks, int depth) {
        this.url = url;
        this.language = language;
        this.headings = headings;
        this.pageLinks = pageLinks;
        this.depth = depth;
    }

    public Page (String url, String failureReson, int depth) {
        this.url = url;
        this.failureReson = failureReson;
        this.depth = depth;
    }

    public boolean isEmpty () {
        return language.isEmpty() && headings.isEmpty();
    }

    public List<String> getPageLinks () {
        return pageLinks;
    }

    public void setPageLinks (List<String> pageLinks) {
        this.pageLinks = pageLinks;
    }

    public String getUrl () {
        return url;
    }

    public String getLanguage () {
        return language.isEmpty() ? "unknown" : language;
    }

    public void setLanguage (String language) {
        this.language = language;
    }

    public Elements getHeadings () {
        return headings;
    }

    public void setHeadings (Elements headings) {
        this.headings = headings;
    }

    public ArrayList<Page> getSubPagesInfo () {
        return subPagesInfo;
    }

    public void addSubPage (Page page) {
        this.subPagesInfo.add(page);
    }

    public void setSubPagesInfo (ArrayList<Page> subPagesInfo) {
        this.subPagesInfo = subPagesInfo;
    }

    public int getDepth () {
        return depth;
    }

    public void setFailureReason (String failureReson) {
        this.failureReson = failureReson;
    }

    public String getFailureReson () {
        return this.failureReson;
    }

    public boolean hasFailed () {
        return this.failureReson != null && !this.failureReson.isEmpty();
    }

    @Override
    public String toString () {
        return "CrawlResult{" +
                "url ='" + url + '\'' +
                ", language='" + language + '\'' +
                ", headings=" + headings.size() +
                ", pageLinks=" + pageLinks.size() +
                ", subPagesInfo=" + subPagesInfo.size() +
                '}';
    }
}