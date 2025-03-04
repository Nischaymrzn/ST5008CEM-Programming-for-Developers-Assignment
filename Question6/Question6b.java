package Question6;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Represents a URL together with its associated crawling depth.
class UrlEntry implements Comparable<UrlEntry> {
  String url; // The URL that will be processed.
  int depth;  // The level of the URL in the crawl (similar to a BFS level).

  UrlEntry(String url, int depth) {
    this.url = url;
    this.depth = depth;
  }

  // Compares URL entries based on their depth for sorting purposes.
  @Override
  public int compareTo(UrlEntry other) {
    return Integer.compare(this.depth, other.depth); // Gives preference to URLs with lower (shallower) depth for breadth-first crawling.
  }
}

public class Question6b {
  private static final int MAX_THREADS = 5; // The upper limit for the number of threads in the pool.
  private static final int MAX_DEPTH = 2;   // The maximum depth allowed for crawling to avoid endless loops.

  // A thread pool for handling multiple crawling tasks concurrently.
  private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
  private static final Queue<UrlEntry> urlQueue = new PriorityQueue<>(); // A priority queue to manage URLs waiting to be crawled.

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter URLs to crawl (type 'done' to finish):");

    // Keep prompting the user for URLs until they type "done".
    while (true) {
      System.out.print("Enter URL: ");
      String input = scanner.nextLine(); // Capture the user's input.
      if (input.equalsIgnoreCase("done")) { // Exit the loop if "done" is entered.
        break;
      }
      // Check that the URL starts with "http://" or "https://".
      if (isValidUrl(input)) {
        urlQueue.add(new UrlEntry(input, 0)); // Add a valid URL to the queue with an initial depth of 0.
      } else {
        System.out.println("Invalid URL. Please enter a valid URL starting with 'http' or 'https'.");
      }
    }
    scanner.close(); // Close the scanner to free resources.

    startCrawling();    // Begin the crawling process.
    shutdownExecutor(); // Shut down the executor service after completing the crawl.
  }

  // Verifies that the URL begins with the correct protocol.
  private static boolean isValidUrl(String url) {
    return url.startsWith("http://") || url.startsWith("https://");
  }

  // Starts the crawling process by dispatching tasks to the thread pool.
  private static void startCrawling() {
    // Continue processing until there are no more URLs in the queue.
    while (!urlQueue.isEmpty()) {
      UrlEntry entry = urlQueue.poll(); // Retrieve the next URL to process.
      if (entry.depth <= MAX_DEPTH) { // Only process the URL if it's within the allowed depth.
        // Schedule a new task in the executor service for this URL.
        executorService.submit(() -> crawl(entry.url, entry.depth));
      }
    }
  }

  // Processes the crawling of a specific URL.
  private static void crawl(String url, int depth) {
    try {
      System.out.println("Crawling: " + url + " [Depth: " + depth + "]");
      String content = fetchUrlContent(url); // Retrieve the content from the URL.
      processContent(url, content); // Process the retrieved content.

      // Additional URLs could be added here dynamically if desired.
    } catch (Exception e) {
      System.out.println("Error crawling " + url + ": " + e.getMessage()); // Report any errors encountered during crawling.
    }
  }

  // Retrieves the content from the specified URL.
  private static String fetchUrlContent(String urlString) throws Exception {
    // Convert the URL string into a URI object (direct URL usage is deprecated).
    URI uri = new URI(urlString);
    URL url = uri.toURL();

    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Establish a connection to the URL.
    connection.setRequestMethod("GET"); // Set the HTTP method to GET.

    // Read the response data from the URL.
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      StringBuilder content = new StringBuilder(); // Use a StringBuilder to accumulate the page's content.
      String line;
      // Append each line of the response to the content.
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
      return content.toString(); // Return the complete content as a string.
    }
  }

  // Handles the processing of the content retrieved from crawling.
  private static void processContent(String url, String content) {
    System.out.println("Crawled: " + url + " [Content Length: " + content.length() + " chars]");
    // Further processing (such as parsing or saving the data) can be added here; currently, it just prints the content length.
  }

  // Gracefully terminates the executor service after all crawling tasks are complete.
  private static void shutdownExecutor() {
    executorService.shutdown(); // Initiate the shutdown of the thread pool.
    try {
      // Wait for up to 30 seconds for tasks to finish; if they don't, force a shutdown.
      if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
        executorService.shutdownNow(); // Immediately terminate tasks if they haven't completed in time.
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow(); // Force a shutdown if an interruption occurs.
    }
  }
}
