import java.util.Scanner; // Import Scanner for user input
import java.util.regex.Pattern; // Import Pattern for regex operations
import java.util.regex.Matcher; // Import Matcher for regex operations

public class DataExtractionAndValidation {

    // Function to find an email address within a given text
    public static String extractEmail(String text) {
        String emailRegex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"; // Define email pattern
        Pattern pattern = Pattern.compile(emailRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(text); // Match the pattern in the text
        if (matcher.find()) { // Check if there's a match
            return matcher.group(); // Return the matched email
        }
        return null; // Return null if no email found
    }

    // Function to find a phone number within a given text
    public static String extractPhoneNumber(String text) {
        String phoneRegex = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}"; // Define phone number pattern
        Pattern pattern = Pattern.compile(phoneRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(text); // Match the pattern in the text
        if (matcher.find()) { // Check if there's a match
            return matcher.group(); // Return the matched phone number
        }
        return null; // Return null if no phone number found
    }

    // Function to find a price within a given text
    public static String extractPrice(String text) {
        String priceRegex = "\\b\\$?\\s?(\\d{1,3}(?:,\\d{3})*|\\d+)(?:\\.\\d{2})?\\s?(CAD|cad)?\\b"; // Define price pattern
        Pattern pattern = Pattern.compile(priceRegex, Pattern.CASE_INSENSITIVE); // Compile regex pattern (case insensitive)
        Matcher matcher = pattern.matcher(text); // Match the pattern in the text

        if (matcher.find()) { // Check if there's a match
            String numericPrice = matcher.group(1).replaceAll(",", ""); // Remove commas from the price
            return numericPrice; // Return the numeric price
        }
        return null; // Return null if no price found
    }

    // Function to validate an email address
    public static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; // Define email validation pattern
        if (email == null) { // Check if email is null
            return false; // Return false if null
        }
        Pattern pattern = Pattern.compile(emailRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(email); // Match the pattern in the email
        return matcher.matches(); // Return whether the email matches the pattern
    }

    // Function to validate a phone number
    public static boolean validatePhoneNumber(String phoneNumber) {
        String phoneRegex = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}"; // Define phone number validation pattern
        Pattern pattern = Pattern.compile(phoneRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(phoneNumber); // Match the pattern in the phone number
        return matcher.matches(); // Return whether the phone number matches the pattern
    }

    // Function to validate names (only letters and spaces)
    public static boolean validateNames(String name) {
        String nameRegex = "^[\\p{L} ]+$"; // Define name validation pattern
        Pattern pattern = Pattern.compile(nameRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(name); // Match the pattern in the name
        return matcher.matches(); // Return whether the name matches the pattern
    }

    // Function to validate prices (only digits and optional currency symbols)
    public static boolean validatePrice(String price) {
        String priceRegex = "^\\$?\\s?\\d+(?:,\\d{3})*(?:\\.\\d{2})?\\s?(CAD|cad)?$"; // Define price validation pattern
        Pattern pattern = Pattern.compile(priceRegex); // Compile regex pattern
        Matcher matcher = pattern.matcher(price); // Match the pattern in the price
        return matcher.matches(); // Return whether the price matches the pattern
    }

    // Function to parse a price string to an integer
    public static int parsePrice(String price) {
        String numericPrice = price.replaceAll("[^\\d.]", ""); // Remove non-digit characters except the decimal point
        double priceDouble = Double.parseDouble(numericPrice); // Convert the numeric price to a double
        return (int) priceDouble; // Convert the double to an integer and return
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter an email and phone number
        System.out.println("Please enter your email ID and phone number in a single line:");
        String userInput = scanner.nextLine();

        // Extract email and phone number from the input
        String email = extractEmail(userInput);
        String phoneNumber = extractPhoneNumber(userInput);

        // Validate the extracted email and phone number
        boolean isEmailValid = validateEmail(email);
        boolean isPhoneNumberValid = validatePhoneNumber(phoneNumber);

        // Display results based on validation
        if (isEmailValid && isPhoneNumberValid) { // If both are valid
            System.out.println("Thanks for the mail ID and phone number, Quotation will be sent to this mail ID: " + email);
        } else if (!isEmailValid) { // If the email is invalid
            System.out.println("Enter valid email ID");
        } else if (!isPhoneNumberValid) { // If the phone number is invalid
            System.out.println("Enter valid phone number");
        }
        scanner.close();
    }
}
