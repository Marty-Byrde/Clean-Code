package translate;

import org.crawler.translate.Translater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TranslationTests {

    @Test
    public void testTranslate () throws IOException, InterruptedException {
        //! This test requires an API key to be set as an environment variable.
        Assertions.assertNotNull(System.getenv().get("API_KEY"), "API_KEY not found. Please set the API-Key as an environment variable.");

        String translation = Translater.translate("Hello World", "de");
        Assertions.assertEquals("Hallo Welt", translation);
    }
}
