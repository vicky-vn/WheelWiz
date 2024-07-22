import java.io.*;
import java.util.*;

class TrieNodeKeer {
    Map<Character, TrieNodeKeer> chldrn;
    boolean isEndOfWordKeer;

    TrieNodeKeer() {
        chldrn = new HashMap<>();
        isEndOfWordKeer = false;
    }
}

class TrieKeer {
    private TrieNodeKeer root;

    TrieKeer() {
        root = new TrieNodeKeer();
    }

    public void insert(String word) {
        TrieNodeKeer current = root;
        for (char chKeer : word.toCharArray()) {
            current = current.chldrn.computeIfAbsent(chKeer, c -> new TrieNodeKeer());
        }
        current.isEndOfWordKeer = true;
    }

    private TrieNodeKeer findNodeWithPrefix(String prfx) {
        TrieNodeKeer crrnt = root;
        for (char chKeer : prfx.toCharArray()) {
            crrnt = crrnt.chldrn.get(chKeer);
            if (crrnt == null) {
                return null;
            }
        }
        return crrnt;
    }

    private void collectWords(TrieNodeKeer node, String prfx, List<String> wordList) {
        if (node == null) {
            return;
        }
        if (node.isEndOfWordKeer) {
            wordList.add(prfx);
        }
        for (Map.Entry<Character, TrieNodeKeer> entry : node.chldrn.entrySet()) {
            collectWords(entry.getValue(), prfx + entry.getKey(), wordList);
        }
    }

    public List<String> getWordsWithPrefix(String prefix) {
        TrieNodeKeer node = findNodeWithPrefix(prefix);
        List<String> wordList = new ArrayList<>();
        collectWords(node, prefix, wordList);
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
                        if (filePath.contains("scraped_chevrolet.csv")) {
                            if (i % 3 == 2) {
                                trie.insert(word);
                            }
                        } else {
                            if (i % 4 == 0) {
                                trie.insert(word);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<String> getAutocompleteSuggestions(String prfx) {
        return trie.getWordsWithPrefix(prfx);
    }

    public void wordCompletion() {
        try (Scanner scnnrKeer = new Scanner(System.in)) {

            String[] filPaths = {
                "../scraped_chevrolet.csv",
                "../scraped_ford.csv",
                "../scraped_hyundai.csv",
                "../scraped_toyota.csv"
            };

            loadVocabulary(filPaths);

            boolean running = true;
            while (running) {
                System.out.print("Enter the prefix of the Category for autocomplete or Press 'Enter' if not sure: ");
                String prfx = scnnrKeer.nextLine();
                List<String> sggstns = getAutocompleteSuggestions(prfx);
                
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
                    } else {
                        System.out.println("Invalid selection. Please try again.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordCompletion wc = new WordCompletion();
        wc.wordCompletion();
    }
}
