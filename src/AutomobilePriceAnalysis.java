import java.io.IOException;
import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) throws IOException {

        SpellCheck spellChecker = new SpellCheck();
        WordCompletion wc = new WordCompletion();

        // Create an instance of TrieInvertedIndex
        TrieInvertedIndex invertedIndex = new TrieInvertedIndex();

        // Create an instance of Trie
        TrieInvertedIndex.Trie trie = new TrieInvertedIndex.Trie();

        // Optionally, load an existing inverted index from CSV (if available)
        invertedIndex.loadInvertedIndexFromCSV(trie);

        SearchFrequency.loadCSVData();
        CarDetails.readCSVsToMap();

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String contactDetails, maxBudgetString, category;

        boolean isUserChoice = false;
        int userChoice, price = 0;

        while (!isUserChoice) {
            boolean isFirstNameValid = false;
            boolean isLastNameValid = false;
            boolean isEmailValid = false;
            boolean isPhoneNumberValid = false;
            boolean isPriceValid = false;

            System.out.print("*******************************************************************************************");
            System.out.println("\n" +
                    "██╗    ██╗██╗  ██╗███████╗███████╗██╗     ██╗    ██╗██╗███████╗\n" +
                    "██║    ██║██║  ██║██╔════╝██╔════╝██║     ██║    ██║██║╚══███╔╝\n" +
                    "██║ █╗ ██║███████║█████╗  █████╗  ██║     ██║ █╗ ██║██║  ███╔╝ \n" +
                    "██║███╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██║███╗██║██║ ███╔╝  \n" +
                    "╚███╔███╔╝██║  ██║███████╗███████╗███████╗╚███╔███╔╝██║███████╗\n" +
                    " ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚══╝╚══╝ ╚═╝╚══════╝" );
            System.out.println("*******************************************************************************************");
            PrintStatements.statementCall(PrintStatements.welcomeMsg);

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Best Deals!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            BestDeal.findAndPrintBestDeals();

            System.out.print("\nTrending Categories : ");
            FrequencyCount.getFrequencyCount();

            System.out.print("\nMost Searched : ");
            SearchFrequency.PrintTreeByFrequency();

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

            category = wc.wordCompletion(input);
            SearchFrequency.addString(category); // To increase the count

            // Spell check here
            String brand = SpellCheck.getBrandFromUser(input, spellChecker);
            if (brand != null) {
                SearchFrequency.addString(brand); // To increase the count

                System.out.println("Details generated for customer => " + firstName + " " + lastName);
                System.out.println("Email: " + email + "\nPhone: " + phoneNumber);

                System.out.print("\n------------------------------------------------------------------------------");
                System.out.print("\nMost Searched : ");
                SearchFrequency.PrintTreeByFrequency();

                CarDetails.getDetails(brand, category, price);

                // Inverted Indexing
                PrintStatements.statementCall(PrintStatements.modelRequest);
                boolean isValidModel = false;
                while (true) {
                    String modelName = input.nextLine().trim().toLowerCase();
                    if (modelName.equals("0")) {
                        break;
                    }
                    if (CarDetails.isValidModel(modelName)) {
                        invertedIndex.printUrlsForKeyword(modelName, trie);
                        isValidModel = true;
                    } else {
                        System.out.println("Invalid model name. Please enter a valid model or press 0 to exit.");
                    }
                    // Prompt user to enter model name again or press 0
                    PrintStatements.statementCall(PrintStatements.modelRequest);
                }

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