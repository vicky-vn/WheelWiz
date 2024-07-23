import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {


        SpellCheck spellChecker = new SpellCheck();
        SearchFrequency.loadCsvData("sf_dataset.csv");

        //List<String> filePaths = Arrays.asList("scraped_ford.csv","scraped_hyundai.csv","scraped_toyota.csv","scraped_chevrolet.csv");
        List<String> filePaths = Arrays.asList("scraped_ford.csv");
        CarDetails.readCSVsToMap(filePaths);

        // Load the inverted index if it exists, otherwise build it
        Map<String, Map<String, Integer>> invertedIndex;
        if (new java.io.File(InvertedIndexing.INDEX_FILE).exists()) {
            //System.out.println("Loading the inverted index from file...");
            invertedIndex = InvertedIndexing.loadInvertedIndex();
        } else {
            //System.out.println("Building the inverted index...");
            invertedIndex = InvertedIndexing.buildInvertedIndex();
            //System.out.println("Inverted index built successfully.");
            InvertedIndexing.saveInvertedIndex(invertedIndex);
        }

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        String contactDetails;

        boolean isUserChoice = false;

        int userChoice,maxBudget;

        while (!isUserChoice) {

            boolean isFirstNameValid = false;
            boolean isLastNameValid = false;
            boolean isEmailValid = false;
            boolean isPhoneNumberValid = false;
            boolean isPriceValid = false;

            //FancyASCII.asciiPrint();

            PrintStatements.statementCall(PrintStatements.welcomeMsg);

            //PrintStatements.statementCall(PrintStatements.trendingCars);
            System.out.print("Trending words : ");
            FrequencyCount.getFrequencyCount();

            System.out.println("Search Freq for Ref");
            SearchFrequency.printTreeByFrequency();

            PrintStatements.statementCall(PrintStatements.welcomeMsg2);

            // First Name Validation
            PrintStatements.statementCall(PrintStatements.firstNameRequest);
            while (!isFirstNameValid) {
                firstName = input.nextLine().trim();
                if (!firstName.isEmpty() && DataExtractionAndValidation.validateNames(firstName)) {
                    isFirstNameValid = true;
                } else {

                }
            }

            // Last Name validation
            PrintStatements.statementCall(PrintStatements.lastNameRequest);
            while (!isLastNameValid) {
                lastName = input.nextLine().trim();
                if (!lastName.isEmpty() && DataExtractionAndValidation.validateNames(lastName)) {
                    isLastNameValid = true;
                } else {

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
            maxBudget = input.nextInt();
            input.nextLine();
            // Page ranking here

            PrintStatements.statementCall(PrintStatements.carCategoryRequest);
            WordCompletion wc = new WordCompletion();
            wc.wordCompletion(input);
            // Word Completion here and search frequency

            PrintStatements.statementCall(PrintStatements.carBrandRequest);

            // Spell check here
            String brand = getBrandFromUser(input, spellChecker);
            SearchFrequency.addString(brand); // To increase the count

            System.out.println("Details generated for customer => " + firstName + " " + lastName);
            System.out.println("Email: " + email + "\nPhone: " + phoneNumber);
            System.out.println("\n********************************");
            System.out.println("********************************");


            CarDetails.getDetails("Ford", "SUVs & Crossovers", maxBudget);// Output

            SearchFrequency.printTreeByFrequency();

            //Partial Inverted Indexing
            String keyword = "corolla";
            InvertedIndexing.printRelevantUrls(keyword, invertedIndex);

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
