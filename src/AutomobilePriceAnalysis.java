import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String contactDetails, email, phoneNumber;

        boolean isUserChoice = false;

        int userChoice, maxBudget;

        while(!isUserChoice) {

            boolean isFirstNameValid = false;
            boolean isLastNameValid = false;
            boolean isEmailValid = false;
            boolean isPhoneNumberValid = false;

            //FancyASCII.asciiPrint();

            PrintStatements.statementCall(PrintStatements.welcomeMsg);

            PrintStatements.statementCall(PrintStatements.trendingCars);

            PrintStatements.statementCall(PrintStatements.welcomeMsg2);

            //Frequency count function will be called your

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

                // Output the results based on validation
                if (isEmailValid && isPhoneNumberValid) {
                    System.out.println("Thanks! Contact details received\nE-mail ID: " + email + "\nMobile Number: " + phoneNumber);
                }
            }

            PrintStatements.statementCall(PrintStatements.maxBudgetRequest);
            // Page ranking here

            PrintStatements.statementCall(PrintStatements.carCategoryRequest);
            // Word Completion here

            PrintStatements.statementCall(PrintStatements.carBrandRequest);
            // Spell check here

            System.out.println("Details generated for " + firstName + " " + lastName);

            PrintStatements.statementCall(PrintStatements.lastStatement);

            userChoice = input.nextInt();

            while(true) {
                if (userChoice == 0) {
                    isUserChoice = true;
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
}
