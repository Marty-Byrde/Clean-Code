package crawl;

import org.crawler.config.Configuration;
import org.crawler.crawl.Crawler;
import org.crawler.crawl.PageInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CrawlerTests {

    Crawler crawler;
    Configuration config = new Configuration("https://orf.at/", 3, new String[]{}, "german");
    PageInfo brokenPageInfo = new PageInfo("invalid.url.at", "", new Elements(), new ArrayList<>(), 0);

    PageInfo mockedPageInfo = mock(PageInfo.class);

    Document mockedDocument = mock(Document.class);
    Element mockedHeading = mock(Element.class);
    Element mockedAnchor = mock(Element.class);


    @BeforeEach
    public void setup () {
        crawler = new Crawler(config);

        
        Element[] mockedPageHeadings = new Element[]{mockedHeading, mockedHeading, mockedHeading};
        Elements el = new Elements();

        //* Attempt to mock the PageInfo object and the related elements, however this information cannot be used since,
        //* the basic methods that take in these arguments are private, and the bigger functions fetch the information directly using the Jsoup.connect.get functionality.

        when(mockedPageInfo.getHeadings()).thenReturn(el);
        when(mockedPageInfo.getUrl()).thenReturn("https://orf.at");
        when(mockedPageInfo.getLanguage()).thenReturn("de");

        when(mockedPageInfo.getPageLinks()).thenReturn(new ArrayList<>());
        when(mockedPageInfo.getDepth()).thenReturn(0);
        when(mockedHeading.tagName()).thenReturn("h2");

        when(mockedHeading.text()).thenReturn("Sample Heading");



        when(mockedAnchor.attr("href")).thenReturn("http://sample.link.at");

        when(mockedDocument.title()).thenReturn("Sample Title");
        when(mockedDocument.select("h1, h2, h3, h4, h5, h6")).thenReturn(new Elements());
        when(mockedDocument.select("a")).thenReturn(new Elements());
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
