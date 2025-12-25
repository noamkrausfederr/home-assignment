package org.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class HtmlParser {

    /**
     * Extracts up to maxUrlsPerPage links from the given HTML.
     */
    public List<String> parseLinks(String html, int maxUrlsPerPage) {
        
        if (html == null || html.isBlank() || maxUrlsPerPage <= 0) {
            return Collections.emptyList();
        }

        // Use lowercase copy of html for case-insensitive tag matching
        String lowerHtml = html.toLowerCase();

        List<String> urls = new ArrayList<>();
        int pos = 0;

        while(pos < html.length() && urls.size() < maxUrlsPerPage) {

            int linkPos = lowerHtml.indexOf("<a", pos);
            if (linkPos == -1) break;

            int tagEnd = html.indexOf(">", linkPos);
            int hrefPos = lowerHtml.indexOf("href=\"", linkPos);

            // Skip invalid or badly formed links
            if ((hrefPos == -1) || (tagEnd != -1 && hrefPos > tagEnd)) {
                if (tagEnd != -1) pos = tagEnd + 1;
                else pos = linkPos + 2;
                continue;
            }

            int urlStart = hrefPos + 6;
            int urlEnd = html.indexOf("\"", urlStart);
            if (urlEnd == -1) {
                pos = urlStart;
                continue;
            }

            String url = html.substring(urlStart, urlEnd).trim();
            if(!url.isEmpty() && !url.startsWith("#")) {
                urls.add(url);
            }

            pos = urlEnd + 1;
        }

        return urls;
    }
}
