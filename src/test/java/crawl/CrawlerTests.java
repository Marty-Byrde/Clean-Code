package crawl;

import org.crawler.adapters.JsoupAdapter;
import org.crawler.config.Configuration;
import org.crawler.crawl.Crawler;
import org.crawler.crawl.Page;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CrawlerTests {

    private Crawler crawler;
    private Configuration mockConfig;

    @BeforeEach
    public void setUp() {
        mockConfig = mock(Configuration.class);
        when(mockConfig.getMaxDepth()).thenReturn(3);
        when(mockConfig.getDomains()).thenReturn(new String[]{});

        crawler = new Crawler(mockConfig);
    }

    @Test
    public void testGetPage_Success() {
        String url = "http://example.com";
        Document mockDocument = mock(Document.class);
        Elements mockHeadings = mock(Elements.class);
        List<String> mockLinks = Arrays.asList("http://example.com/page1", "http://example.com/page2");

        try (MockedStatic<JsoupAdapter> jsoupAdapterMock = mockStatic(JsoupAdapter.class)) {
            jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(url)).thenReturn(mockDocument);
            jsoupAdapterMock.when(() -> JsoupAdapter.getLanguage(mockDocument)).thenReturn("en");
            jsoupAdapterMock.when(() -> JsoupAdapter.getHeadings(mockDocument)).thenReturn(mockHeadings);
            jsoupAdapterMock.when(() -> JsoupAdapter.getLinks(mockDocument)).thenReturn(mockLinks);

            Page page = crawler.getPage(url, 0);

            assertNotNull(page);
            assertEquals(url, page.getUrl());
            assertEquals("en", page.getLanguage());
            assertEquals(mockHeadings, page.getHeadings());
            assertEquals(mockLinks, page.getPageLinks());
        }
    }

    @Test
    public void testGetPage_Failure() {
        String url = "http://example.com";
        IOException mockException = new IOException();

        try (MockedStatic<JsoupAdapter> jsoupAdapterMock = mockStatic(JsoupAdapter.class)) {
            jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(url)).thenThrow(mockException);

            Page page = crawler.getPage(url, 0);

            assertNotNull(page);
            assertEquals(url, page.getUrl());
            assertEquals(0, page.getHeadings().size());
            assertEquals(0, page.getPageLinks().size());
        }
    }
}
