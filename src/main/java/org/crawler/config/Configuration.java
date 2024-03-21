package org.crawler.config;

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
