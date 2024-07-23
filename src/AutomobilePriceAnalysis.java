import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {


        SpellCheck spellChecker = new SpellCheck();

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String contactDetails;

        boolean isUserChoice = false;

        int userChoice, maxBudget;

        while (!isUserChoice) {

            boolean isFirstNameValid = false;
            boolean isLastNameValid = false;
            boolean isEmailValid = false;
            boolean isPhoneNumberValid = false;

            //FancyASCII.asciiPrint();

            PrintStatements.statementCall(PrintStatements.welcomeMsg);

            //PrintStatements.statementCall(PrintStatements.trendingCars);
            System.out.print("Trending words : ");
            FrequencyCount.getFrequencyCount();


            PrintStatements.statementCall(PrintStatements.welcomeMsg2);

            PrintStatements.statementCall(PrintStatements.firstNameRequest);
            // First Name validation
            while (!isFirstNameValid) {
                firstName = input.nextLine().trim();
                if (!firstName.isEmpty() && DataExtractionAndValidation.validateNames(firstName)) {
                    isFirstNameValid = true;
                } else {
                    PrintStatements.statementCall(PrintStatements.firstNameRequest);
                }
            }

            PrintStatements.statementCall(PrintStatements.lastNameRequest);
            // Last Name validation
            while (!isLastNameValid) {
                lastName = input.nextLine().trim();
                if (!lastName.isEmpty() && DataExtractionAndValidation.validateNames(lastName)) {
                    isLastNameValid = true;
                } else {
                    PrintStatements.statementCall(PrintStatements.lastNameRequest);
                }
            }

            // Extracting mail, phone and validating it
            while (!isEmailValid || !isPhoneNumberValid) {
                PrintStatements.statementCall(PrintStatements.contactDetailsRequest);
                contactDetails = input.nextLine();

                // Extract the email and phone number
                email = DataExtractionAndValidation.extractEmail(contactDetails);
                phoneNumber = DataExtractionAndValidation.extractPhoneNumber(contactDetails);

                // Check if email and phone number were found
                if (email == null) {
                    PrintStatements.statementCall(PrintStatements.invalidEmail);
                }
                if (phoneNumber == null) {
                    PrintStatements.statementCall(PrintStatements.invalidPhone);
                }

                // Validate the email and phone number if they are not null
                isEmailValid = email != null && DataExtractionAndValidation.validateEmail(email);
                isPhoneNumberValid = phoneNumber != null && DataExtractionAndValidation.validatePhoneNumber(phoneNumber);
            }

            PrintStatements.statementCall(PrintStatements.maxBudgetRequest);
            // Page ranking here

            PrintStatements.statementCall(PrintStatements.carCategoryRequest);
            WordCompletion wc = new WordCompletion();
            wc.wordCompletion(input);
            // Word Completion here and search frequency

            PrintStatements.statementCall(PrintStatements.carBrandRequest);

            // Spell check here
            String brand = getBrandFromUser(input, spellChecker);

            System.out.println("Details generated for customer => " + firstName + " " + lastName);
            System.out.println("Email: " + email + "\nPhone: " + phoneNumber);

            PrintStatements.statementCall(PrintStatements.lastStatement);

            userChoice = input.nextInt();

            while (true) {
                if (userChoice == 0) {
                    isUserChoice = true;
                    PrintStatements.statementCall(PrintStatements.farewellStatement);
                    break;
                } else if (userChoice == 1) {
                    break;
                } else {
                    PrintStatements.statementCall(PrintStatements.invalidEntry);
                    PrintStatements.statementCall(PrintStatements.lastStatement);
                    userChoice = input.nextInt();
                    input.nextLine();
                }
            }
        }
        input.close();
    }

    private static String getBrandFromUser(Scanner input, SpellCheck spellChecker) {
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
                    continue;
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