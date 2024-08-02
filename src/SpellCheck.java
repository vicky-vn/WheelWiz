import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.FileReader;

public class SpellCheck {

    private Set<String> vocabulary; // Set to store vocabulary words

    // Constructor that initializes with a default file if none is provided
    public SpellCheck() {
        this("CarBrands.txt"); // Use default file for car brands
    }

    // Constructor that takes a file path to load the vocabulary
    public SpellCheck(String filePath) {
        vocabulary = new HashSet<>(); // Initialize the vocabulary set
        loadVocabulary(filePath); // Load vocabulary from the specified file
    }

    // Method to load vocabulary from a file
    private void loadVocabulary(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Try to open and read the file
            String line;
            while ((line = br.readLine()) != null) { // Read each line from the file
                vocabulary.add(line.trim().toLowerCase()); // Add the word to the vocabulary set
            }
        } catch (IOException e) { // Handle any IO exceptions
            System.err.println("An error occurred while loading vocabulary from the file: " + e.getMessage());
        }
    }

    // Method to get the closest matching word from the vocabulary
    public String getClosestMatch(String input) {
        String lowerInput = input.toLowerCase(); // Convert input to lowercase
        for (String word : vocabulary) { // Loop through each word in the vocabulary
            if (word.equalsIgnoreCase(lowerInput)) { // Check if the word matches exactly
                return word;
            }
            if (levenshteinDistance(word, lowerInput) <= 2) { // Check if the word is within a Levenshtein distance of 2
                return word;
            }
        }
        return null; // Return null if no close match is found
    }

    // Method to calculate Levenshtein distance between two strings
    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1]; // Create a DP table
        for (int i = 0; i <= a.length(); i++) { // Loop through string a
            for (int j = 0; j <= b.length(); j++) { // Loop through string b
                if (i == 0) { // If the first string is empty
                    dp[i][j] = j; // Initialize dp table
                } else if (j == 0) { // If the second string is empty
                    dp[i][j] = i; // Initialize dp table
                } else { // Calculate minimum cost
                    dp[i][j] = Math.min(dp[i - 1][j - 1]
                                    + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()]; // Return the Levenshtein distance
    }

    // Method to interact with the user and get a valid car brand name
    public static String getBrandFromUser(Scanner input, SpellCheck spellChecker) {
        while (true) { // Loop until a valid brand is obtained
            System.out.println("\nPlease enter the brand name:"); // Prompt user for brand name
            String brand = input.nextLine().trim(); // Read and trim the input
            String closestMatch = spellChecker.getClosestMatch(brand); // Get closest match from the spell checker

            if (closestMatch == null) { // If no close match is found
                System.out.println("We don't have information for this particular brand."); // Inform the user
                System.out.println("Do you want to check another brand? (Yes/No)"); // Ask if they want to try again
                String response = input.nextLine().trim(); // Read user's response
                if (response.equalsIgnoreCase("No")) { // If the user says no
                    return null; // Exit the method
                }
            } else if (!brand.equalsIgnoreCase(closestMatch)) { // If a close match is found but not exact
                System.out.println("Did you mean: " + closestMatch + "? (Yes/No)"); // Ask the user if they meant the close match
                String response = input.nextLine().trim(); // Read user's response
                if (response.equalsIgnoreCase("Yes")) { // If the user confirms
                    return closestMatch; // Return the close match
                } else { // If the user does not confirm
                    System.out.println("Please enter the correct brand name:"); // Ask for the correct brand name
                }
            } else { // If an exact match is found
                return brand; // Return the brand
            }
        }
    }

    // Main method to run the spell check
    public static void main(String[] args) {
        SpellCheck spellChecker = new SpellCheck(); // Create a spell checker with the default file
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in); // Create a scanner to read user input
            String brand = getBrandFromUser(scanner, spellChecker); // Get a valid brand from the user
            if (brand != null) { // If a valid brand is obtained
                System.out.println("You selected: " + brand); // Print the selected brand
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close(); // Close the scanner
            }
        }
    }
}
