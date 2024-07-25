import java.util.Map;
import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {

        SpellCheck spellChecker = new SpellCheck();
        SearchFrequency.loadCsvData();
        CarDetails.readCSVsToMap();

        // Load the inverted index if it exists, otherwise build it
        Map<String, Map<String, Integer>> invertedIndex;
        if (new java.io.File(InvertedIndexing.varForIndexFile).exists()) {
            invertedIndex = InvertedIndexing.loadInvertedIndex();
        } else {
            invertedIndex = InvertedIndexing.buildInvertedIndex();
            InvertedIndexing.saveInvertedIndex(invertedIndex);
        }

        // Set the inverted index in CarDetails
        CarDetails.setInvertedIndex(invertedIndex);

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String contactDetails, maxBudgetString;

        boolean isUserChoice = false;
        int userChoice, price = 0;

        while (!isUserChoice) {
            boolean isFirstNameValid = false;
            boolean isLastNameValid = false;
            boolean isEmailValid = false;
            boolean isPhoneNumberValid = false;
            boolean isPriceValid = false;

            PrintStatements.statementCall(PrintStatements.welcomeMsg);

            System.out.print("Trending words : ");
            FrequencyCount.getFrequencyCount();

            System.out.print("Most Searched : ");
            SearchFrequency.printTreeByFrequency();

            CarDetails.getDetails("Mitsubishi", "SUV & Crossover", 35000);

            PrintStatements.statementCall(PrintStatements.welcomeMsg2);

            // First Name Validation
            PrintStatements.statementCall(PrintStatements.firstNameRequest);
            while (!isFirstNameValid) {
                firstName = input.nextLine().trim();
                if (!firstName.isEmpty() && DataExtractionAndValidation.validateNames(firstName)) {
                    isFirstNameValid = true;
                } else {
                    PrintStatements.statementCall(PrintStatements.validNameRequest);
                }
            }

            // Last Name Validation
            PrintStatements.statementCall(PrintStatements.lastNameRequest);
            while (!isLastNameValid) {
                lastName = input.nextLine().trim();
                if (!lastName.isEmpty() && DataExtractionAndValidation.validateNames(lastName)) {
                    isLastNameValid = true;
                } else {
                    PrintStatements.statementCall(PrintStatements.validNameRequest);
                }
            }

            // Extracting email and phone number and validating them
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
            // Price extraction and validation
            while (!isPriceValid) {
                maxBudgetString = input.nextLine().trim();
                String extractedPrice = DataExtractionAndValidation.extractPrice(maxBudgetString);
                if (extractedPrice != null && DataExtractionAndValidation.validatePrice(extractedPrice)) {
                    isPriceValid = true;
                    price = DataExtractionAndValidation.parsePrice(extractedPrice);
                } else {
                    PrintStatements.statementCall(PrintStatements.validBudgetRequest);
                }
            }

            PrintStatements.statementCall(PrintStatements.carCategoryRequest);
            WordCompletion wc = new WordCompletion();
            String category = wc.wordCompletion(input);

            PrintStatements.statementCall(PrintStatements.carBrandRequest);

            // Spell check here
            String brand = SpellCheck.getBrandFromUser(input, spellChecker);
            SearchFrequency.addString(brand); // To increase the count

            System.out.println("Details generated for customer => " + firstName + " " + lastName);
            System.out.println("Email: " + email + "\nPhone: " + phoneNumber);
            System.out.println("\n********************************");
            System.out.println("********************************");

            SearchFrequency.printTreeByFrequency();

            PrintStatements.statementCall(PrintStatements.lastStatement);

            while (!input.hasNextInt()) {
                input.next(); // Consume the invalid input
                PrintStatements.statementCall(PrintStatements.invalidEntry);
            }
            userChoice = input.nextInt();
            input.nextLine(); // Consume the newline character

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
                    while (!input.hasNextInt()) {
                        input.next(); // Consume the invalid input
                        PrintStatements.statementCall(PrintStatements.invalidEntry);
                    }
                    userChoice = input.nextInt();
                    input.nextLine(); // Consume the newline character
                }
            }
        }
        input.close();
    }
}
