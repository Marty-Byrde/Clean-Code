package config;

import org.crawler.config.Configuration;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfigurationTests {

    @Test
    public void testRequestConfiguration() throws IOException {
        String simulatedUserInput = "https://www.example.com/\n2\nexample.com\nen\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        BufferedReader reader = mock(BufferedReader.class);

        when(reader.readLine()).thenReturn(
                "https://www.example.com/",
                "2",
                "example.com",
                "en"
        );

        Configuration config = Configuration.requestConfiguration();

        assertNotNull(config);
        assertArrayEquals(new String[]{"https://www.example.com/"}, config.getUrls());
        assertEquals(2, config.getMaxDepth());
        assertArrayEquals(new String[]{"example.com"}, config.getDomains());
    }

}
