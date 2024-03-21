package org.crawler.crawl;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class PageInfo {
    private String url;
    private String language;
    private Elements headings;
    private List<String> pageLinks;
    private ArrayList<PageInfo> subPagesInfo = new ArrayList<>();
    private int depth;

    public PageInfo (String url, String language, Elements headings, List<String> pageLinks, int depth) {
        this.url = url;
        this.language = language;
        this.headings = headings;
        this.pageLinks = pageLinks;
        this.depth = depth;
    }

    public boolean isBroken () {
        return language.isEmpty();
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
        return language;
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

    public ArrayList<PageInfo> getSubPagesInfo () {
        return subPagesInfo;
    }

    public void setSubPagesInfo (ArrayList<PageInfo> subPagesInfo) {
        this.subPagesInfo = subPagesInfo;
    }

    public int getDepth () {
        return depth;
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