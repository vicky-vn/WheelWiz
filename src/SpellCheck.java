import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellCheck {

    private Set<String> vocabulary;

    public SpellCheck() {
        this("CarBrands.txt"); // Default to carbrands.txt if no filePath is provided
    }

    public SpellCheck(String filePath) {
        vocabulary = new HashSet<>();
        loadVocabulary(filePath);
    }

    private void loadVocabulary(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                vocabulary.add(line.trim().toLowerCase());
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
            System.out.println("\nPlease enter the brand name:");
            String brand = input.nextLine().trim();
            String closestMatch = spellChecker.getClosestMatch(brand);

            if (closestMatch == null) {
                System.out.println("We don't have information for this particular brand.");
                System.out.println("Do you want to check another brand? (Yes/No)");
                String response = input.nextLine().trim();
                if (response.equalsIgnoreCase("No")) {
                    return null;
                }
            } else if (!brand.equalsIgnoreCase(closestMatch)) {
                System.out.println("Did you mean: " + closestMatch + "? (Yes/No)");
                String response = input.nextLine().trim();
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

    public static void main(String[] args) {
        SpellCheck spellChecker = new SpellCheck();
        Scanner scanner = new Scanner(System.in);
        String brand = getBrandFromUser(scanner, spellChecker);
        if (brand != null) {
            System.out.println("You selected: " + brand);
        }
        scanner.close();
    }
}