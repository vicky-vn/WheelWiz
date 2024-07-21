import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SpellCheck {

    private Set<String> vocabulary;

    public SpellCheck(String[] csvFilePaths) {
        vocabulary = new HashSet<>();
        for (String filePath : csvFilePaths) {
            loadVocabulary(filePath);
        }
    }

    private void loadVocabulary(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming the first column contains model names and the second column contains categories
                vocabulary.add(values[0].trim().toLowerCase());
                if (values.length > 1) {
                    vocabulary.add(values[1].trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClosestMatch(String input) {
        String lowerInput = input.toLowerCase();
        for (String word : vocabulary) {
            if (word.equalsIgnoreCase(lowerInput)) {
                return word;
            }
            // Implement a more sophisticated spell check algorithm as needed
            if (levenshteinDistance(word, lowerInput) <= 2) {
                return word;
            }
        }
        return null;
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1]
                                    + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }
}