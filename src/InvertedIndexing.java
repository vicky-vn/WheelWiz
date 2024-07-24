import com.opencsv.CSVReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

public class InvertedIndexing {

    private static final String varForURLDataset = "crawled_urls.csv"; // CSV file containing URLs
    public static final String varForIndexFile = "inverted_index.ser"; // File to save the inverted index
    private static final Set<String> varForStoppers = new HashSet<>(Arrays.asList("a", "an", "and", "the", "to", "of", "in", "on", "for", "with", "at", "by"));

    public static Map<String, Map<String, Integer>> buildInvertedIndex() {
        List<String> urls = readUrls();
        Map<String, Map<String, Integer>> index = new HashMap<>();
        int processedCount = 0;
        int totalUrls = urls.size();

        for (String url : urls) {
            try {
                String[] keywords = extractKeywordsFromUrl(url);
                for (String keyword : keywords) {
                    index.computeIfAbsent(keyword, k -> new HashMap<>())
                            .merge(url, 1, Integer::sum);
                }
            } catch (IOException exc) {
                System.err.println("Error occurred in fetching content from URL: " + url + " - " + exc.getMessage());
            }
            processedCount++;
            if (processedCount % 10 == 0 || processedCount == totalUrls) {
                System.out.println("Processed " + processedCount + " of " + totalUrls + " URLs.");
            }
        }

        return index;
    }

    public static String[] extractKeywordsFromUrl(String varForUrl) throws IOException {
        Document varForDoc = Jsoup.connect(varForUrl).timeout(10 * 1000).get();
        String text = varForDoc.body().text();
        return extractKeywordsFromText(text);
    }

    public static String[] extractKeywordsFromText(String VarFortext) {
        // Adjust regex to split by non-word characters
        String[] words = VarFortext.split("\\W+");
        List<String> keywords = new ArrayList<>();
        for (String word : words) {
            if (!varForStoppers.contains(word.toLowerCase()) && word.length() > 1) {
                // Take only the first word if it contains space or hyphen
                String firstWord = word.split("[\\s-]")[0].toLowerCase();
                if (!keywords.contains(firstWord)) {
                    keywords.add(firstWord);
                }
            }
        }
        return keywords.toArray(new String[0]);
    }

    public static String pageRanker(String keyword, Map<String, Map<String, Integer>> invertedIndex) {
        // Normalize the keyword to handle spaces or hyphens
        String normalizedKeyword = keyword.split("[\\s-]")[0].toLowerCase();
        Map<String, Integer> urlCounts = invertedIndex.get(normalizedKeyword);

        if (urlCounts == null || urlCounts.isEmpty()) {
            return "No URLs found for keyword \"" + normalizedKeyword + "\".";
        }

        // Find the URL with the highest occurrence
        String topUrl = null;
        int maxOccurrences = 0;

        for (Map.Entry<String, Integer> entry : urlCounts.entrySet()) {
            if (entry.getValue() > maxOccurrences) {
                topUrl = entry.getKey();
                maxOccurrences = entry.getValue();
            }
        }

        return topUrl != null ? topUrl : "No top URL found for keyword \"" + normalizedKeyword + "\".";
    }

    private static List<String> readUrls() {
        List<String> urls = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(InvertedIndexing.varForURLDataset))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                urls.add(line[0]);
            }
        } catch (IOException exc) {
            System.err.println("Error occurred in reading URLs file: " + exc.getMessage());
        }
        return urls;
    }

    public static void saveInvertedIndex(Map<String, Map<String, Integer>> index) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(varForIndexFile))) {
            oos.writeObject(index);
        } catch (IOException exc) {
            System.err.println("Error occurred in saving inverted index: " + exc.getMessage());
        }
    }

    public static Map<String, Map<String, Integer>> loadInvertedIndex() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(varForIndexFile))) {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Integer>> index = (Map<String, Map<String, Integer>>) ois.readObject();
            return index;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred in loading inverted index: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
