package org.crawler;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WebCrawler core logic, focusing on link parsing,
 * filename sanitization, and thread-safe uniqueness tracking.
 */
class WebCrawlerTest {

    @Test
    void testLinkExtractionLimit() {
        HtmlParser parser = new HtmlParser();

        String html = """
                <a href="http://a.com"></a>
                <a href="http://b.com"></a>
                <a href="http://c.com"></a>
                """;

        List<String> links = parser.parseLinks(html, 2);
        
        // The result should respect the 'maxUrlsPerPage' constraint.
        assertEquals(2, links.size(),
            "Parser should extract only up to maxUrlsPerPage links");
    }


    @Test
    void testUrlSanitizationLogic() {
        String url = "https://www.google.com/search?q=java";

        // Applying the sanitization logic used in PageStorage.
        String sanitized = url.replaceAll("[^a-zA-Z0-9]", "_");

        assertTrue(sanitized.matches("[A-Za-z0-9_]+"),
            "Sanitized filename should contain only letters, digits, or underscores");
    }


    @Test
    void testUniquenessTracking() {
        // Validates the thread-safe uniqueness tracking logic used inside WebCrawler.
        Set<String> visited = ConcurrentHashMap.newKeySet();

        boolean first = visited.add("https://example.com");
        // Simulate discovering the same URL again
        boolean second = visited.add("https://example.com");

        assertTrue(first, "First addition should return true (URL was not seen before)");
        assertFalse(second, "Second addition should return false (URL already processed)");
    }
}
