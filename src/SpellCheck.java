import java.util.HashSet;
import java.util.Set;

public class SpellCheck {

    private Set<String> vocabulary;

    public SpellCheck() {
        vocabulary = new HashSet<>();
        // Add the fixed list of car brands to the vocabulary
        vocabulary.add("ford");
        vocabulary.add("chevrolet");
        vocabulary.add("hyundai");
        vocabulary.add("toyota");
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