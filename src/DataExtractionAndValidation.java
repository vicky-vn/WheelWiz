import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataExtractionAndValidation {

    // Method to extract email from text
    public static String extractEmail(String text) {
        String emailRegex = "([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Method to extract phone number from text
    public static String extractPhoneNumber(String text) {
        String phoneRegex = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    // Method to extract price from text
    public static String extractPrice(String text) {
        // Regular expression to match prices in various formats
        String priceRegex = "\\b\\$?\\s?(\\d{1,3}(?:,\\d{3})*|\\d+)(?:\\.\\d{2})?\\s?(CAD|cad)?\\b";
        Pattern pattern = Pattern.compile(priceRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            // Extract the numeric part from the matched group
            String numericPrice = matcher.group(1).replaceAll(",", "");
            return numericPrice;
        }
        return null;
    }

    // Method to validate email
    public static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to validate phone number
    public static boolean validatePhoneNumber(String phoneNumber) {
        String phoneRegex = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Method to validate firstName & lastName
    public static boolean validateNames(String name) {
        String nameRegex = "^[\\p{L} ]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    // Method to validate prices containing only digits and optional currency symbols
    public static boolean validatePrice(String price) {
        // Regular expression to match prices with only digits and optional USD or CAD format
        String priceRegex = "^\\$?\\s?\\d+(?:,\\d{3})*(?:\\.\\d{2})?\\s?(CAD|cad)?$";
        Pattern pattern = Pattern.compile(priceRegex);
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    // Method to parse the price string to an integer
    public static int parsePrice(String price) {
        // Remove non-numeric characters except for decimal point
        String numericPrice = price.replaceAll("[^\\d.]", "");
        // Convert to integer
        double priceDouble = Double.parseDouble(numericPrice);
        return (int) priceDouble;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompting user to enter email and phone number
        System.out.println("Please enter your email ID and phone number in a single line:");
        String userInput = scanner.nextLine();

        // Extracting email and phone number from user input
        String email = extractEmail(userInput);
        String phoneNumber = extractPhoneNumber(userInput);

        // Validating extracted email and phone number
        boolean isEmailValid = validateEmail(email);
        boolean isPhoneNumberValid = validatePhoneNumber(phoneNumber);

        // Output the results based on validation
        if (isEmailValid && isPhoneNumberValid) {
            System.out.println("Thanks for the mail ID and phone number, Quotation will be sent to this mail ID: " + email);
        } else if (!isEmailValid) {
            System.out.println("Enter valid email ID");
        } else if (!isPhoneNumberValid) {
            System.out.println("Enter valid phone number");
        }
        scanner.close();
    }
}