package org.crawler;

import java.io.IOException;
import java.nio.file.*;

public class PageStorage {

    /**
     * Saves the downloaded HTML page to a file using the
     * '<depth>/<url>.html' naming convention.
     */
    
    public static void save(String html, int depth, String url) throws IOException {
        // Clean URL to create a filesystem-safe filename
        String validName = url.replaceAll("[^a-zA-Z0-9]", "_");

        // Create directory for the given crawl depth if it does not exist
        Path dir = Paths.get(String.valueOf(depth));
        Files.createDirectories(dir);

        // Construct full file path: <depth>/<url>.html
        Path file = dir.resolve(validName + ".html");

        // Write HTML content to file
        Files.writeString(file, html);
    }
}
