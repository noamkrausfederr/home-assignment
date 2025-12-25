package org.crawler;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler {
     
    private final CrawlConfig config;
    private final PageDownloader downloader;
    private final HtmlParser parser;

    // Uses virtual threads to run multiple crawl tasks in parallel
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    // Tracks visited URLs when cross-level uniqueness is enabled
    private final Set<String> processedUrls = ConcurrentHashMap.newKeySet();

    public WebCrawler(CrawlConfig config, PageDownloader downloader, HtmlParser parser) {
        this.config = config;
        this.downloader = downloader;
        this.parser = parser;
    }

    /** 
     * Starts the crawling process from the initial URL and 
     * proceeds level by level up to the configured depth.
     */
    public void start() {

        // URLs to process at the current depth level
        Set<String> currentDepthLevel = Set.of(config.startUrl());

        for (int depth = 0; depth <= config.depth(); depth++) {

            // Collect URLs discovered for the next depth level
            Set<String> nextDepthLevel = ConcurrentHashMap.newKeySet();
            List<CompletableFuture<Void>> tasks = new ArrayList<>();

            for (String url : currentDepthLevel) {

                // If uniqueness is enabled and this URL was already processed, skip it.
                if(config.uniqueness() && !processedUrls.add(url)) continue;

                int currentDepth = depth;
                tasks.add(CompletableFuture.runAsync(() -> processUrl(url, currentDepth, nextDepthLevel),executor));

            }

            // Wait for all URLs at this depth to finish processing
            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

            if(nextDepthLevel.isEmpty()) break;
            currentDepthLevel = nextDepthLevel;
        }

        // Shut down executor after crawl completion
        executor.shutdown();
    }

    /**
     * Downloads a single URL, saves its content 
     * and collects links for the next depth level.
     */
    private void processUrl(String url, int depth, Set<String> nextLevelCollector) {
        try {

            String html = downloader.download(url);

            PageStorage.save(html, depth, url);

            if (depth < config.depth()) { 
                URI base = URI.create(url);

                // List of up to maxUrlsPerPage links found in the HTML page.
                List<String> discoveredLinks = parser.parseLinks(html, config.maxUrlsPerPage());

                for (String link: discoveredLinks) {
                    try {
                        URI resolved = base.resolve(link);

                        // Only add valid absolute URLs
                        if (resolved.getScheme() != null) {
                            nextLevelCollector.add(resolved.toString());
                        }
                    } catch (IllegalArgumentException ignored) {
                        // Ignore invalid or badly formed links
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process " + url + ": " + e.getMessage());
        }
    }
}
