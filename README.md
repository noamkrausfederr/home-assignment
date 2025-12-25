## Web Crawler Assignment

Web Crawler is a Java-based project created as part of a home assignment. The program crawls web pages starting from a given URL, downloads page content, and discovers additional links level by level while respecting configurable limits on crawl depth, number of links per page, and URL uniqueness.

This project demonstrates concurrent programming, clean architecture, and safe multithreaded crawling using modern Java features.

## Key Features
- **Java 21 Virtual Threads:** Uses Executors.newVirtualThreadPerTaskExecutor() to efficiently process multiple URLs in parallel.
- **Breadth-First Search (BFS) Logic:** Processes all URLs at a specific depth before moving to the next level, ensuring a structured crawl.
- **Cross-Level Uniqueness:** Implements a thread-safe ConcurrentHashMap-based set to track visited URLs and prevent redundant processing.
- **Automated Redirect Handling:** HTTP client follows redirects automatically to ensure successful page downloads.
- **Robust Error Handling:** Gracefully handles failed downloads, restricted access, and invalid URLs without stopping the crawl.

## Built With
- Java 21
- Gradle (Groovy DSL)
- Java HttpClient
- Java Virtual Threads

## Installation & Execution
**Prerequisites**
- JDK 21 or higher
- Internet connection for crawling

**Steps**
1. Clone the repository
```git clone https://github.com/noamkrausfederr/home-assignment.git```
2. Run the crawler using Gradle - The application requires four command-line arguments.
```./gradlew run --args="<url> <maxUrlsPerPage> <depth> <uniqueness>"```
Example: To crawl Google with a limit of 5 links per page, up to depth 2, and ensuring every URL is unique:
```./gradlew run --args="https://www.google.com 5 2 true"```

**Argument Reference**
- **url (String):** The starting seed URL.
- **maxUrlsPerPage (int):** Max number of links to extract from a single page.
- **depth (int):** How many levels deep to crawl (0-indexed).
- **uniqueness (boolean):** If true, prevents visiting the same URL twice across different levels.

## Output Structure
Downloaded pages are sanitized and saved locally by depth:

```text
├── 0/
│   └── www-google-com.html
├── 1/
│   ├── news-google-com.html
│   └── maps-google-com.html
└── 2/
    └── ...
```

## Usage
The crawler starts from the given URL and processes pages level by level. Each downloaded page is saved locally using the following structure: <depth>/<sanitized-url>.html

The program supports:
- **Breadth-First Search (BFS):** Processes all URLs at a given depth before moving to the next level.
- **Link Limiting:** Limits the number of extracted links per page.
- **Depth Control:** Configurable crawl depth (0-indexed).
- **URL Uniqueness:** Optional cross-level URL uniqueness to prevent redundant processing.
- **High Concurrency:** Parallel page downloads using Java 21 Virtual Threads.


## Quality Assurance & Testing
The project includes a suite of unit tests to ensure the reliability of the core logic. 

**Key areas tested**
- **Link Extraction:** Verifies the crawler respects the `maxUrlsPerPage` limit.
- **URL Sanitization:** Ensures generated filenames are clean and safe to save on the computer.
- **Uniqueness Tracking:** Confirms that the crawler correctly avoids visiting the same URL twice.

**To run the tests:**
```./gradlew test```