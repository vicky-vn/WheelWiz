import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String firstName, lastName, email,contactDetails, phoneNumber;

        int maxBudget, mobileNumber;

        FancyASCII.asciiPrint();

        PrintStatements.statementCall(PrintStatements.welcomeMsg);

        PrintStatements.statementCall(PrintStatements.Trendingcars);
        //Frequency count function will be called your

        PrintStatements.statementCall(PrintStatements.firstNameRequest);
        firstName = input.nextLine();
        // Name data validation should be called here

        PrintStatements.statementCall(PrintStatements.lastNameRequest);
        lastName = input.nextLine();
        // Last name data validation should be called here


        PrintStatements.statementCall(PrintStatements.contactDetailsRequest);
        // Extract the mail and number
        // Validate the mail and number
        boolean isEmailValid = false;
        boolean isPhoneNumberValid = false;

        while (!isEmailValid || !isPhoneNumberValid) {
            PrintStatements.statementCall(PrintStatements.contactDetailsRequest);
            contactDetails = input.nextLine();

            // Extract the email and phone number
            email = DataExtraction.extractEmail(contactDetails);
            phoneNumber = DataExtraction.extractPhoneNumber(contactDetails);

            // Check if email and phone number were found
            if (email == null) {
                System.out.println("No valid email found in the input.");
            }
            if (phoneNumber == null) {
                System.out.println("No valid phone number found in the input.");
            }

            // Validate the email and phone number if they are not null
            isEmailValid = email != null && DataExtraction.validateEmail(email);
            isPhoneNumberValid = phoneNumber != null && DataExtraction.validatePhoneNumber(phoneNumber);

            // Output the results based on validation
            if (!isEmailValid && !isPhoneNumberValid) {
                System.out.println("Enter valid phone number and email");
            } else if (!isEmailValid) {
                System.out.println("Enter valid email ID");
            } else if (!isPhoneNumberValid) {
                System.out.println("Enter valid phone number");
            }
            else if (isPhoneNumberValid && isPhoneNumberValid ) {
                System.out.println("Thanks for the mail ID and phone number, Quotation will be sent to this mail ID: " + email);
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


    }
}
