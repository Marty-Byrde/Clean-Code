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
import java.util.stream.IntStream;

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

    @ParameterizedTest
    @CsvSource({
            "true, http://example.com",
            "true, https://example.com",
            "false, /internal/link"
    })
    public void testIsExternalUrl(boolean isExternal, String url) {
        assertEquals(isExternal, crawler.isExternalUrl(url));
    }

    @Test
    public void testFilterLinks() {
        List<String> links = Arrays.asList("http://example.com", "https://example.com", "https://example.com", "/internal/link", "http://example.net");
        List<String> expectedLinks = Arrays.asList("https://example.com", "/internal/link", "http://example.net");

        List<String> filteredLinks = crawler.filterLinks("http://example.com", links);
        assertEquals(expectedLinks.size(), filteredLinks.size());
        assertTrue(filteredLinks.containsAll(expectedLinks));
    }

    @ParameterizedTest
    @CsvSource({
            "http://example.com, http://example.com/subpage, http://example.com/subsubpage",
            "http://example2.com, http://example2.com/page1, http://example2.com/page2",
            "http://example3.com, http://example3.com/section1, http://example3.com/section2"
    })
    public void testRecursiveCrawl(String mainUrl, String subUrl1, String subUrl2) {
        List<String> urls = Arrays.asList(mainUrl, subUrl1, subUrl2);
        List<Document> mockedDocuments = Arrays.asList(mock(Document.class), mock(Document.class), mock(Document.class));
        List<Elements> mockedHeadings = Arrays.asList(mock(Elements.class), mock(Elements.class), mock(Elements.class));
        List<List<String>> mockedLinks = Arrays.asList(
                Collections.singletonList(subUrl1),
                Collections.singletonList(subUrl2),
                Collections.emptyList()
        );

        List<Page> mockedPages = Arrays.asList(
                new Page(mainUrl, "en", mockedHeadings.get(0), mockedLinks.get(0), 0),
                new Page(subUrl1, "en", mockedHeadings.get(1), mockedLinks.get(1), 1),
                new Page(subUrl2, "en", mockedHeadings.get(2), mockedLinks.get(2), 2)
        );

        try (MockedStatic<JsoupAdapter> jsoupAdapterMock = mockStatic(JsoupAdapter.class)) {
            IntStream.range(0, urls.size()).forEach(i -> {
                jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(urls.get(i))).thenReturn(mockedDocuments.get(i));
                jsoupAdapterMock.when(() -> JsoupAdapter.getLanguage(mockedDocuments.get(i))).thenReturn("en");
                jsoupAdapterMock.when(() -> JsoupAdapter.getHeadings(mockedDocuments.get(i))).thenReturn(mockedHeadings.get(i));
                jsoupAdapterMock.when(() -> JsoupAdapter.getLinks(mockedDocuments.get(i))).thenReturn(mockedLinks.get(i));
            });

            Crawler spyCrawler = spy(crawler);

            for (int i = 0; i < urls.size(); i++) {
                doReturn(mockedPages.get(i)).when(spyCrawler).getPage(urls.get(i), mockedPages.get(i).getDepth());
            }

            Page result = spyCrawler.recursiveCrawl(mainUrl, 0);

            assertNotNull(result);
            assertEquals(1, result.getSubPagesInfo().size());

            Page subPage1 = result.getSubPagesInfo().get(0);
            assertEquals(subUrl1, subPage1.getUrl());
            assertEquals(1, subPage1.getSubPagesInfo().size());

            Page subPage2 = subPage1.getSubPagesInfo().get(0);
            assertEquals(subUrl2, subPage2.getUrl());
            assertTrue(subPage2.getSubPagesInfo().isEmpty());

            for (int i = 0; i < urls.size(); i++) {
                verify(spyCrawler, times(1)).getPage(urls.get(i), mockedPages.get(i).getDepth());
            }
        }
    }
}
