import java.io.*;
import java.util.*;

class TrieNodeKeer {
    Map<Character, TrieNodeKeer> chldrn;
    boolean isEndOfWordKeer;
    String word; // Store the actual word at the end node

    TrieNodeKeer() {
        chldrn = new HashMap<>();
        isEndOfWordKeer = false;
        word = null;
    }
}

class TrieKeer {
    private TrieNodeKeer root;

    TrieKeer() {
        root = new TrieNodeKeer();
    }

    public void insert(String word) {
        TrieNodeKeer current = root;
        String lowerCaseWord = word.toLowerCase(); // Convert word to lowercase for the Trie structure
        for (char chKeer : lowerCaseWord.toCharArray()) {
            current = current.chldrn.computeIfAbsent(chKeer, c -> new TrieNodeKeer());
        }
        current.isEndOfWordKeer = true;
        current.word = word; // Store the original word at the end node
    }

    private TrieNodeKeer findNodeWithPrefix(String prfx) {
        TrieNodeKeer crrnt = root;
        prfx = prfx.toLowerCase(); // Convert prefix to lowercase
        for (char chKeer : prfx.toCharArray()) {
            crrnt = crrnt.chldrn.get(chKeer);
            if (crrnt == null) {
                return null;
            }
        }
        return crrnt;
    }

    private void collectWords(TrieNodeKeer node, List<String> wordList) {
        if (node == null) {
            return;
        }
        if (node.isEndOfWordKeer) {
            wordList.add(node.word); // Add the original word
        }
        for (Map.Entry<Character, TrieNodeKeer> entry : node.chldrn.entrySet()) {
            collectWords(entry.getValue(), wordList);
        }
    }

    public List<String> getWordsWithPrefix(String prefix) {
        TrieNodeKeer node = findNodeWithPrefix(prefix);
        List<String> wordList = new ArrayList<>();
        collectWords(node, wordList);
        return wordList;
    }
}

public class WordCompletion {

    private TrieKeer trie = new TrieKeer();

    public void loadVocabulary(String[] filePaths) throws IOException {
        for (String filePath : filePaths) {
            try (BufferedReader brKeer = new BufferedReader(new FileReader(filePath))) {
                String lnKeer;
                while ((lnKeer = brKeer.readLine()) != null) {
                    String[] wordsKeer = lnKeer.split(",");
                    for (int i = 0; i < wordsKeer.length; i++) {
                        String word = wordsKeer[i].trim().replace("\"", "");
                        if (i % 5 == 4 && !word.equals("Category")) {
                            trie.insert(word);
                        }
                    }
                }
            }
        }
    }

    public List<String> getAutocompleteSuggestions(String prfx) {
        return trie.getWordsWithPrefix(prfx);
    }

    public String wordCompletion(Scanner scnnrKeer) {
        String[] filPaths = {
                "scraped_chevrolet.csv",
                "scraped_mitsubishi.csv",
                "scraped_hyundai.csv",
                "scraped_nissan.csv"
        };

        try {
            loadVocabulary(filPaths);

            boolean running = true;
            String category = "";
            System.out.print("\nEnter the prefix of the Category for autocomplete or Press 'Enter' if not sure: ");
            String prfx = scnnrKeer.nextLine();
            List<String> sggstns = getAutocompleteSuggestions(prfx);
            while (running) {
                if (sggstns.isEmpty()) {
                    System.out.println("No suggestions found. Please try a different prefix.");
                } else {
                    System.out.println("Autocomplete Suggestions:");
                    for (int i = 0; i < sggstns.size(); i++) {
                        System.out.println((i + 1) + ". " + sggstns.get(i));
                    }
                    System.out.print("Select a suggestion by entering the number: ");
                    int selection = scnnrKeer.nextInt();
                    scnnrKeer.nextLine(); // Consume the newline character

                    if (selection >= 1 && selection <= sggstns.size()) {
                        System.out.println("You selected: " + sggstns.get(selection - 1));
                        running = false; // Exit the loop if a valid selection is made
                        category = sggstns.get(selection - 1);
                    } else {
                        System.out.println("Invalid selection. Please try again.");
                    }
                }
            }
            return category;
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Err"; // Exit if there is an error loading the vocabulary
        }
    }
}