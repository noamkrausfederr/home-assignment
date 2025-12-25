package org.crawler;

/**
 * Holds all configuration values needed to run the crawler.
 */
public record CrawlConfig(
    String startUrl, 
    int maxUrlsPerPage, 
    int depth, 
    boolean uniqueness) {}

