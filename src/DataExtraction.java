import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataExtraction {

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
        } else if (!isEmailValid && !isPhoneNumberValid) {
            System.out.println("Enter valid phone number and email");
        } else if (!isEmailValid) {
            System.out.println("Enter valid email ID");
        } else if (!isPhoneNumberValid) {
            System.out.println("Enter valid phone number");
        }

        scanner.close();
    }
}