import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {

        SpellCheck spellChecker = new SpellCheck();
        WordCompletion wc = new WordCompletion();

        // Create an instance of TrieInvertedIndex
        TrieInvertedIndex invertedIndex = new TrieInvertedIndex();

        // Create an instance of Trie
        TrieInvertedIndex.Trie trie = new TrieInvertedIndex.Trie();

        // Optionally, load an existing inverted index from CSV (if available)
        invertedIndex.loadInvertedIndexFromCSV(trie);

        SearchFrequency.loadCsvData();
        CarDetails.readCSVsToMap();

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String contactDetails, maxBudgetString, category, modelName;

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

            System.out.println("Email: " + email);
            System.out.println("Mobile: " + phoneNumber);

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

            System.out.println("Price : " + price);

            PrintStatements.statementCall(PrintStatements.carCategoryRequest);
            category = wc.wordCompletion(input);
            SearchFrequency.addString(category); // To increase the count

            PrintStatements.statementCall(PrintStatements.carBrandRequest);

            // Spell check here
            String brand = SpellCheck.getBrandFromUser(input, spellChecker);
            if (brand != null) {
                SearchFrequency.addString(brand); // To increase the count

                System.out.println("Details generated for customer => " + firstName + " " + lastName);
                System.out.println("Email: " + email + "\nPhone: " + phoneNumber);
                System.out.println("\n********************************");
                System.out.println("********************************");

                SearchFrequency.printTreeByFrequency();

                CarDetails.getDetails(brand, category, price);

                // Inverted Indexing
                PrintStatements.statementCall(PrintStatements.modelRequest);
                modelName = input.nextLine().trim().toLowerCase();
                invertedIndex.printUrlsForKeyword(modelName, trie);

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
            } else {
                System.out.println("No valid brand selected. Exiting.");
                break;
            }
        }
        input.close();
    }
}
