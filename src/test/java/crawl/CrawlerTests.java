package crawl;

import org.crawler.config.Configuration;
import org.crawler.crawl.Crawler;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CrawlerTests {

    Crawler crawler;
    Configuration config = new Configuration("https://orf.at/", 3, new String[]{}, "german");


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

}
