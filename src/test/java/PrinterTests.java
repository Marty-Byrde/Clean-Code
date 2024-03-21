import org.crawler.crawl.PageInfo;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;


public class PrinterTests {
    Element mockedElement = Mockito.mock(Element.class);
    PageInfo mockedValidPageInfo = Mockito.mock(PageInfo.class);
    PageInfo mockedInvalidPageInfo = Mockito.mock(PageInfo.class);
    ArrayList<String> pageLinks = new ArrayList<>(Arrays.asList("http://link1.at", "http://link2.at"));


    @BeforeEach
    public void setUp() {
        when(mockedElement.tagName()).thenReturn("h2");
        when(mockedElement.text()).thenReturn("I am a sample heading.");

        when(mockedInvalidPageInfo.getHeadings()).thenReturn(new Elements());
        when(mockedInvalidPageInfo.getUrl()).thenReturn("http://link.at");
        when(mockedInvalidPageInfo.getSubPagesInfo()).thenReturn(new ArrayList<>());

        when(mockedValidPageInfo.getPageLinks()).thenReturn(pageLinks);
        when(mockedValidPageInfo.getSubPagesInfo()).thenReturn(new ArrayList<>());
        when(mockedValidPageInfo.isBroken()).thenReturn(false);
        when(mockedValidPageInfo.getUrl()).thenReturn("http://link.at");
        when(mockedValidPageInfo.getLanguage()).thenReturn("de");

        Elements mockedElements = new Elements();
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);
        mockedElements.add(mockedElement);

        when(mockedValidPageInfo.getHeadings()).thenReturn(mockedElements);
    }
}
