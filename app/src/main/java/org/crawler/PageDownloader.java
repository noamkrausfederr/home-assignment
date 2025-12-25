package org.crawler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageDownloader {

    private final HttpClient client;

    public PageDownloader() { 
        this.client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build(); 
    }

    /**
     * Downloads the HTML content of the given URL.
     * Only successful (HTTP 200) responses are considered valid.
     */
    public String download(String url) throws Exception {

        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        // Treat non-200 responses as download failures
        int status = response.statusCode();
        if (status != 200) {
            throw new RuntimeException("Failed to download " + url + " (status: " + status + ")");
        }

        return response.body();
    }
}
