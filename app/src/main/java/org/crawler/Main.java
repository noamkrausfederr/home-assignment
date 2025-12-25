package org.crawler;

public class Main {

    public static void main(String[] args) {

        // Validate that all required arguments are provided
        if (args.length != 4) {
            System.err.println("Usage: <startUrl> <maxUrlsPerPage> <depth> <uniqueness>");
            System.exit(1);
        } 

        try {
            // Parse command line arguments
            String startUrl = args[0];
            int maxUrlsPerPage = Integer.parseInt(args[1]);
            int depth = Integer.parseInt(args[2]);
            boolean uniqueness = Boolean.parseBoolean(args[3]);

            // Immutable configuration shared across crawler components
            CrawlConfig config = new CrawlConfig(startUrl, maxUrlsPerPage, depth, uniqueness);

            // Crawler dependencies
            PageDownloader downloader = new PageDownloader();
            HtmlParser parser = new HtmlParser();
            WebCrawler crawler = new WebCrawler(config, downloader, parser);

            // Start the crawling process
            System.out.println("Starting crawl at: " + startUrl);
            crawler.start();
            System.out.println("Crawl completed.");

        } catch (NumberFormatException e) {
            // Handle invalid numeric arguments
            System.err.println("Error: maxUrlsPerPage and Depth must be integers.");
            System.exit(1);
            
        } catch (Exception e) {
            // Prevent unexpected termination
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1);
        }

    }
}
