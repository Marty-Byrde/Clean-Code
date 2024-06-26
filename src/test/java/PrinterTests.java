import org.crawler.Printer;
import org.crawler.crawl.Page;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class PrinterTests {
    Element mockedElement = Mockito.mock(Element.class);
    Page mockedValidPageInfo = Mockito.mock(Page.class);
    Page mockedInvalidPageInfo = Mockito.mock(Page.class);
    ArrayList<String> pageLinks = new ArrayList<>(Arrays.asList("http://link1.at", "http://link2.at"));


    @BeforeEach
    public void setUp () {
        when(mockedElement.tagName()).thenReturn("h2");
        when(mockedElement.text()).thenReturn("I am a sample heading.");

        when(mockedInvalidPageInfo.getHeadings()).thenReturn(new Elements());
        when(mockedInvalidPageInfo.getUrl()).thenReturn("http://link.at");
        when(mockedInvalidPageInfo.getSubPagesInfo()).thenReturn(new ArrayList<>());

        when(mockedValidPageInfo.getPageLinks()).thenReturn(pageLinks);
        when(mockedValidPageInfo.getSubPagesInfo()).thenReturn(new ArrayList<>());
        when(mockedValidPageInfo.isEmpty()).thenReturn(false);
        when(mockedValidPageInfo.getUrl()).thenReturn("http://link.at");
        when(mockedValidPageInfo.getLanguage()).thenReturn("de");

        Elements mockedElements = new Elements();
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);

        when(mockedValidPageInfo.getHeadings()).thenReturn(mockedElements);
    }

    @Test
    public void testCreateReport_Depth_0 () throws IOException {
        when(mockedValidPageInfo.getDepth()).thenReturn(0);

        List<String> report = Printer.createReport(mockedValidPageInfo);

        assertEquals("input: <http://link.at>", report.get(0));
        assertEquals("language: <de>", report.get(1));
        assertEquals("## I am a sample heading.", report.get(2));
    }

    @Test
    public void testCreateReport_Depth_1 () throws IOException {
        when(mockedValidPageInfo.getDepth()).thenReturn(1);

        List<String> report = Printer.createReport(mockedValidPageInfo);

        assertEquals("input: <http://link.at>", report.get(0));
        assertEquals("language: <de>", report.get(1));
        assertEquals("## -> I am a sample heading.", report.get(2));
    }

    @Test
    public void testCreateReport_InvalidPageInfo () throws IOException {
        when(mockedInvalidPageInfo.isEmpty()).thenReturn(true);
        when(mockedInvalidPageInfo.getLanguage()).thenReturn("");


        List<String> report = Printer.createReport(mockedInvalidPageInfo);

        assertEquals(3, report.size());
        assertEquals("input: <http://link.at>", report.get(0));
        assertEquals("language: <>", report.get(1));
        assertEquals("Broken page!", report.get(2));
    }

}
