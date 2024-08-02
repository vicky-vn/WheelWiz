import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrieInvertedIndex {

    private static final String varForURLDataset = "crawled_urls.csv"; // CSV file containing URLs
    private static final String varForIndexCSV = "inverted_index.csv"; // CSV file for the inverted index
    private static final Set<String> varForStoppers = new HashSet<>(Arrays.asList("a", "an", "and", "the", "to", "of", "in", "on", "for", "with", "at", "by"));

    public static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> urlCounts = new HashMap<>();
    }

    public static class Trie {
        TrieNode root = new TrieNode();

        public void insert(String word, String url, int count) {
            if (word == null || word.isEmpty()) {
                throw new IllegalArgumentException("Input word cannot be null or empty");
            }
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.urlCounts.put(url, node.urlCounts.getOrDefault(url, 0) + count);
        }

        public Map<String, Integer> search(String word) {
            if (word == null || word.isEmpty()) {
                throw new IllegalArgumentException("Input word cannot be null or empty");
            }
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.get(c);
                if (node == null) {
                    return Collections.emptyMap();
                }
            }
            return node.urlCounts;
        }
    }

    public void buildInvertedIndex(Trie trie) {
        List<String> urls = readUrls();
        int processedCount = 0;
        int totalUrls = urls.size();

        for (String url : urls) {
            try {
                String[] keywords = extractKeywordsFromUrl(url);
                for (String keyword : keywords) {
                    trie.insert(keyword, url, 1);
                }
            } catch (IOException exc) {
                System.err.println("Error occurred in fetching content from URL: " + url + " - " + exc.getMessage());
            } catch (IllegalArgumentException exc) {
                System.err.println("Error processing keyword from URL: " + url + " - " + exc.getMessage());
            }
            processedCount++;
            if (processedCount % 10 == 0 || processedCount == totalUrls) {
                System.out.println("Processed " + processedCount + " of " + totalUrls + " URLs.");
            }
        }
    }

    public void loadInvertedIndexFromCSV(Trie trie) {
        try (CSVReader reader = new CSVReader(new FileReader(varForIndexCSV))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line[0].equals("Keyword")) continue; // Skip header
                String keyword = line[0];
                String url = line[1];
                int occurrences = Integer.parseInt(line[2]);
                trie.insert(keyword, url, occurrences);
            }
        } catch (IOException exc) {
            System.err.println("Error occurred in reading inverted index CSV file: " + exc.getMessage());
        } catch (IllegalArgumentException exc) {
            System.err.println("Error processing entry in inverted index CSV file: " + exc.getMessage());
        }
    }

    public String[] extractKeywordsFromUrl(String varForUrl) throws IOException {
        Document varForDoc = Jsoup.connect(varForUrl).timeout(10 * 1000).get();
        String text = varForDoc.body().text();
        return extractKeywordsFromText(text);
    }

    public String[] extractKeywordsFromText(String varForText) {
        if (varForText == null || varForText.isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty");
        }
        String[] words = varForText.split("\\W+");
        List<String> keywords = new ArrayList<>();
        for (String word : words) {
            String normalizedWord = normalizeKeyword(word);
            if (!varForStoppers.contains(normalizedWord) && normalizedWord.length() > 1) {
                keywords.add(normalizedWord);
            }
        }
        return keywords.toArray(new String[0]);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        String normalized = keyword.toLowerCase().split("[\\W_]+")[0];
        return normalized;
    }

    private List<String> readUrls() {
        List<String> urls = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(varForURLDataset))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                urls.add(line[0]);
            }
        } catch (IOException exc) {
            System.err.println("Error occurred in reading URLs file: " + exc.getMessage());
        }
        return urls;
    }

    public void createInvertedIndexCSV(Trie trie) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(varForIndexCSV))) {
            writer.writeNext(new String[]{"Keyword", "URL", "Occurrences"});
            writeTrieToCSV(trie.root, new StringBuilder(), writer);
        } catch (IOException e) {
            System.err.println("Error occurred in creating inverted index CSV: " + e.getMessage());
        }
    }

    private void writeTrieToCSV(TrieNode node, StringBuilder prefix, CSVWriter writer) {
        if (!node.urlCounts.isEmpty()) {
            for (Map.Entry<String, Integer> entry : node.urlCounts.entrySet()) {
                writer.writeNext(new String[]{prefix.toString(), entry.getKey(), String.valueOf(entry.getValue())});
            }
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            writeTrieToCSV(entry.getValue(), prefix, writer);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public void printUrlsForKeyword(String keyword, Trie trie) {
        try {
            Map<String, Integer> urlsForKeyword = trie.search(normalizeKeyword(keyword));
            if (urlsForKeyword.isEmpty()) {
                System.out.println("No URLs found for keyword: " + keyword);
            } else {
                System.out.println("Keyword: " + keyword);
                for (Map.Entry<String, Integer> entry : urlsForKeyword.entrySet()) {
                    System.out.println("URL: " + entry.getKey() + ", Occurrences: " + entry.getValue());
                }
            }
        } catch (IllegalArgumentException exc) {
            System.err.println("Error searching for keyword: " + exc.getMessage());
        }
    }

    public static void main(String[] args) {
        TrieInvertedIndex trieInvertedIndex = new TrieInvertedIndex();
        Trie trie = new Trie();

        // Load inverted index from CSV (if exists)
        trieInvertedIndex.loadInvertedIndexFromCSV(trie);

        // Build the inverted index from URLs
        trieInvertedIndex.buildInvertedIndex(trie);

        // Save the inverted index to CSV
        trieInvertedIndex.createInvertedIndexCSV(trie);

        // Search for a keyword
        trieInvertedIndex.printUrlsForKeyword("Mirage", trie); // Replace "example" with your test keyword
    }
}