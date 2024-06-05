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
        Document mockMainDocument = mock(Document.class);
        Document mockSubDocument1 = mock(Document.class);
        Document mockSubDocument2 = mock(Document.class);

        Elements mockMainHeadings = mock(Elements.class);
        Elements mockSubHeadings1 = mock(Elements.class);
        Elements mockSubHeadings2 = mock(Elements.class);

        List<String> mockMainLinks = Collections.singletonList(subUrl1);
        List<String> mockSubLinks1 = Collections.singletonList(subUrl2);
        List<String> mockSubLinks2 = Collections.emptyList();

        Page mockMainPage = new Page(mainUrl, "en", mockMainHeadings, mockMainLinks, 0);
        Page mockSubPage1 = new Page(subUrl1, "en", mockSubHeadings1, mockSubLinks1, 1);
        Page mockSubPage2 = new Page(subUrl2, "en", mockSubHeadings2, mockSubLinks2, 2);

        try (MockedStatic<JsoupAdapter> jsoupAdapterMock = mockStatic(JsoupAdapter.class)) {
            jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(mainUrl)).thenReturn(mockMainDocument);
            jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(subUrl1)).thenReturn(mockSubDocument1);
            jsoupAdapterMock.when(() -> JsoupAdapter.fetchDocument(subUrl2)).thenReturn(mockSubDocument2);

            jsoupAdapterMock.when(() -> JsoupAdapter.getLanguage(mockMainDocument)).thenReturn("en");
            jsoupAdapterMock.when(() -> JsoupAdapter.getLanguage(mockSubDocument1)).thenReturn("en");
            jsoupAdapterMock.when(() -> JsoupAdapter.getLanguage(mockSubDocument2)).thenReturn("en");

            jsoupAdapterMock.when(() -> JsoupAdapter.getHeadings(mockMainDocument)).thenReturn(mockMainHeadings);
            jsoupAdapterMock.when(() -> JsoupAdapter.getHeadings(mockSubDocument1)).thenReturn(mockSubHeadings1);
            jsoupAdapterMock.when(() -> JsoupAdapter.getHeadings(mockSubDocument2)).thenReturn(mockSubHeadings2);

            jsoupAdapterMock.when(() -> JsoupAdapter.getLinks(mockMainDocument)).thenReturn(mockMainLinks);
            jsoupAdapterMock.when(() -> JsoupAdapter.getLinks(mockSubDocument1)).thenReturn(mockSubLinks1);
            jsoupAdapterMock.when(() -> JsoupAdapter.getLinks(mockSubDocument2)).thenReturn(mockSubLinks2);

            Crawler spyCrawler = spy(crawler);

            doReturn(mockMainPage).when(spyCrawler).getPage(mainUrl, 0);
            doReturn(mockSubPage1).when(spyCrawler).getPage(subUrl1, 1);
            doReturn(mockSubPage2).when(spyCrawler).getPage(subUrl2, 2);

            Page result = spyCrawler.recursiveCrawl(mainUrl, 0);

            assertNotNull(result);
            assertEquals(1, result.getSubPagesInfo().size());

            Page subPage1 = result.getSubPagesInfo().get(0);
            assertEquals(subUrl1, subPage1.getUrl());
            assertEquals(1, subPage1.getSubPagesInfo().size());

            Page subPage2 = subPage1.getSubPagesInfo().get(0);
            assertEquals(subUrl2, subPage2.getUrl());
            assertTrue(subPage2.getSubPagesInfo().isEmpty());

            verify(spyCrawler, times(1)).getPage(mainUrl, 0);
            verify(spyCrawler, times(1)).getPage(subUrl1, 1);
            verify(spyCrawler, times(1)).getPage(subUrl2, 2);
        }
    }
}
