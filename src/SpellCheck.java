import java.util.HashSet;
import java.util.Scanner;
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

    public static String getBrandFromUser(Scanner input, SpellCheck spellChecker) {
        while (true) {
            String brand = input.nextLine();
            String closestMatch = spellChecker.getClosestMatch(brand);

            if (closestMatch == null) {
                System.out.println("We don't have information for this particular brand.");
                System.out.println("Do you want to check another brand? (Yes/No)");
                String response = input.nextLine();
                if (response.equalsIgnoreCase("No")) {
                    return null;
                } else {
                    System.out.println("Please enter the brand name:");
                }
            } else if (!brand.equalsIgnoreCase(closestMatch)) {
                System.out.println("Did you mean: " + closestMatch + "? (Yes/No)");
                String response = input.nextLine();
                if (response.equalsIgnoreCase("Yes")) {
                    return closestMatch;
                } else {
                    System.out.println("Please enter the correct brand name:");
                }
            } else {
                return brand;
            }
        }
    }
}
