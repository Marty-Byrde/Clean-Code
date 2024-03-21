package crawl;

import org.crawler.config.Configuration;
import org.crawler.crawl.Crawler;
import org.crawler.crawl.PageInfo;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

class CrawlerTests {

    Crawler crawler;
    Configuration config = new Configuration("https://orf.at/", 3, new String[]{}, "german");
    PageInfo brokenPageInfo = new PageInfo("invalid.url.at", "", new Elements(), new ArrayList<>(), 0);

    @BeforeEach
    public void setup () {
        crawler = new Crawler(config);
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://orf.at/", "https://www.derstandard.at/", "invalid.url.at"})
    public void test_getDocument (String url) {
        Document document = crawler.getDocument(url);

        if (url.startsWith("https://") || url.startsWith("http://")) Assertions.assertNotNull(document);
        else Assertions.assertNull(document);
    }

    @Test
    public void test_invalid_retrievePageInfo () {
        PageInfo info = crawler.retrievePageInfo("invalid.url.at", new String[]{}, 0);

        Assertions.assertEquals(brokenPageInfo.getSubPagesInfo(), info.getSubPagesInfo());
        Assertions.assertEquals(brokenPageInfo.getUrl(), info.getUrl());
        Assertions.assertEquals(brokenPageInfo.getLanguage(), info.getLanguage());
        Assertions.assertEquals(brokenPageInfo.getHeadings(), info.getHeadings());
        Assertions.assertEquals(brokenPageInfo.getPageLinks(), info.getPageLinks());
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://orf.at/", "https://www.derstandard.at/"})
    public void test_retrievePageInfo (String url) {
        PageInfo info = crawler.retrievePageInfo(url, new String[]{}, 0);

        Assertions.assertEquals(url, info.getUrl());
        Assertions.assertNotEquals("", info.getLanguage());
        Assertions.assertNotEquals(0, info.getHeadings().size());
        Assertions.assertNotEquals(0, info.getPageLinks().size());
    }

}
