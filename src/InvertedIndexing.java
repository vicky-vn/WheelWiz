import com.opencsv.CSVReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

public class InvertedIndexing implements Serializable {

    private static final String URL_FILE = "crawled_urls.csv"; // CSV file containing URLs
    public static final String INDEX_FILE = "inverted_index.ser"; // File to save the inverted index
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList("a", "an", "and", "the", "to", "of", "in", "on", "for", "with", "at", "by"));

    public static void main(String[] args) {
        // This is left empty or used for testing
    }

    public static Map<String, Map<String, Integer>> buildInvertedIndex() {
        List<String> urls = readUrls(URL_FILE);
        Map<String, Map<String, Integer>> index = new HashMap<>();
        int processedCount = 0;
        int totalUrls = urls.size();

        System.out.println("Total URLs to process: " + totalUrls);

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
        System.out.println("Fetching content from URL: " + varForUrl);
        Document document = Jsoup.connect(varForUrl).timeout(10 * 1000).get();
        String text = document.body().text();
        return extractKeywordsFromText(text);
    }

    public static String[] extractKeywordsFromText(String VarFortext) {
        String[] words = VarFortext.split("\\W+");
        List<String> keywords = new ArrayList<>();
        for (String word : words) {
            if (!STOP_WORDS.contains(word.toLowerCase()) && word.length() > 1) {
                keywords.add(word.toLowerCase());
            }
        }
        return keywords.toArray(new String[0]);
    }

    public static void printRelevantUrls(String keyword, Map<String, Map<String, Integer>> invertedIndex) {
        Map<String, Integer> urlCounts = invertedIndex.get(keyword.toLowerCase());

        if (urlCounts == null || urlCounts.isEmpty()) {
            System.out.println("No URLs found for keyword \"" + keyword + "\".");
            return;
        }

        System.out.println("URLs containing the keyword \"" + keyword + "\":");
        for (Map.Entry<String, Integer> entry : urlCounts.entrySet()) {
            System.out.println("{ URL : " + entry.getKey() + ", Occurrences :" + entry.getValue() + "}");
        }
    }

    private static List<String> readUrls(String fileName) {
        List<String> urls = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INDEX_FILE))) {
            oos.writeObject(index);
            System.out.println("Inverted index saved to file.");
        } catch (IOException exc) {
            System.err.println("Error occurred in saving inverted index: " + exc.getMessage());
        }
    }

    public static Map<String, Map<String, Integer>> loadInvertedIndex() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INDEX_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Integer>> index = (Map<String, Map<String, Integer>>) ois.readObject();
            //System.out.println("Inverted index loaded from file.");
            return index;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred in loading inverted index: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static void printTopWordFrequencies(String url, int topN) {
        try {
            Document document = Jsoup.connect(url).timeout(10 * 1000).get();
            String text = document.body().text();
            Map<String, Integer> wordFrequencies = getWordFrequencies(text);
            System.out.println("Top " + topN + " word frequencies for URL: " + url);
            wordFrequencies.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(topN)
                    .forEach(entry -> System.out.print("| " + entry.getKey() + " "+ entry.getValue() + " | "));
            System.out.println();
        } catch (IOException exc) {
            System.err.println("Error occurred in fetching content from URL: " + url + " - " + exc.getMessage());
        }
    }

    public static Map<String, Integer> getWordFrequencies(String text) {
        String[] words = text.split("\\W+");
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String word : words) {
            String lowerCaseWord = word.toLowerCase();
            if (!STOP_WORDS.contains(lowerCaseWord) && lowerCaseWord.length() > 1) {
                wordFrequencies.put(lowerCaseWord, wordFrequencies.getOrDefault(lowerCaseWord, 0) + 1);
            }
        }
        return wordFrequencies;
    }
}
