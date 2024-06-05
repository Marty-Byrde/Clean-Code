package adapters;

import org.crawler.adapters.JsoupAdapter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class JsoupAdapterTests {


    private Document mockDocument;
    private Elements mockElements;

    @BeforeEach
    void setUp() {
        mockDocument = mock(Document.class);
        mockElements = mock(Elements.class);
    }

    @Test
    void testFetchDocument() throws IOException {
        try (MockedStatic<Jsoup> mockedJsoup = Mockito.mockStatic(Jsoup.class)) {
            String url = "http://example.com";
            Connection mockConnection = mock(Connection.class);
            Document mockDocument = mock(Document.class);

            mockedJsoup.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.title()).thenReturn("Example Domain");

            Document doc = JsoupAdapter.fetchDocument(url);

            assertNotNull(doc);
            assertEquals("Example Domain", doc.title());
        }
    }

    @Test
    void testGetLanguage() {
        when(mockDocument.select("html")).thenReturn(mockElements);
        when(mockElements.attr("lang")).thenReturn("en");

        String language = JsoupAdapter.getLanguage(mockDocument);

        assertNotNull(language);
        assertEquals("en", language);
    }

    @Test
    void testGetHeadings() {
        when(mockDocument.select("h1, h2, h3, h4, h5, h6")).thenReturn(mockElements);
        Elements headings = JsoupAdapter.getHeadings(mockDocument);

        assertNotNull(headings);
        verify(mockDocument).select("h1, h2, h3, h4, h5, h6");
    }



}
