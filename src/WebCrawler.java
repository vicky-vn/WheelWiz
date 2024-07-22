import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WebCrawler {
    private final Set<String> visited = new HashSet<>();
    private final Queue<String> toVisit = new LinkedList<>();
    private final Map<String, Set<String>> linkGraph = new HashMap<>();
    private final Map<String, Double> pageRanks = new HashMap<>();
    private final Map<String, Set<String>> invertedIndex = new HashMap<>();
    private final double dampingFactor = 0.85;
    private final int maxIterations = 20;

    public void crawl(String startUrl, int maxPages) {
        toVisit.add(startUrl);

        while (!toVisit.isEmpty() && visited.size() < maxPages) {
            String url = toVisit.poll();
            if (visited.contains(url)) {
                continue;
            }

            try {
                Document doc = Jsoup.connect(url).get();
                visited.add(url);
                System.out.println("Visited: " + url);

                // Extract and index text from the page
                String text = doc.body().text();
                indexText(text, url);

                // Extract links and build the link graph
                Elements links = doc.select("a[href]");
                Set<String> outLinks = new HashSet<>();
                for (Element link : links) {
                    String nextUrl = link.attr("abs:href");
                    if (!visited.contains(nextUrl)) {
                        toVisit.add(nextUrl);
                    }
                    outLinks.add(nextUrl);
                }

                linkGraph.put(url, outLinks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished crawling. Starting PageRank calculation...");
        calculatePageRank();
        writeToCSV();
    }

    private void indexText(String text, String url) {
        String[] words = text.split("\\W+"); // Split by non-word characters
        for (String word : words) {
            word = word.toLowerCase(); // Normalize to lowercase
            if (word.isEmpty()) {
                continue;
            }
            invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(url);
        }
    }

    private void calculatePageRank() {
        int numPages = linkGraph.size();
        if (numPages == 0) {
            System.out.println("No pages to rank.");
            return;
        }

        // Initialize page ranks
        for (String page : linkGraph.keySet()) {
            pageRanks.put(page, 1.0 / numPages);
        }

        for (int i = 0; i < maxIterations; i++) {
            Map<String, Double> newPageRanks = new HashMap<>();
            double baseRank = (1 - dampingFactor) / numPages;

            for (String page : linkGraph.keySet()) {
                double rankSum = 0.0;
                for (Map.Entry<String, Set<String>> entry : linkGraph.entrySet()) {
                    String otherPage = entry.getKey();
                    if (entry.getValue().contains(page)) {
                        rankSum += pageRanks.get(otherPage) / entry.getValue().size();
                    }
                }
                newPageRanks.put(page, baseRank + dampingFactor * rankSum);
            }

            pageRanks.putAll(newPageRanks);
        }

        System.out.println("PageRank calculation complete.");
    }

    private void writeToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("page_rank.csv"))) {
            // Write PageRank CSV
            writer.writeNext(new String[]{"URL", "PageRank"});
            for (Map.Entry<String, Double> entry : pageRanks.entrySet()) {
                writer.writeNext(new String[]{entry.getKey(), entry.getValue().toString()});
            }
            System.out.println("PageRank data written to page_rank.csv");

            // Write Inverted Index CSV
            writer.writeNext(new String[]{"Word", "URLs"});
            for (Map.Entry<String, Set<String>> entry : invertedIndex.entrySet()) {
                String urls = String.join(", ", entry.getValue());
                writer.writeNext(new String[]{entry.getKey(), urls});
            }
            System.out.println("Inverted index data written to page_rank.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        crawler.crawl("https://www.toyota.ca/", 50); // Example URL and max pages
    }
}
