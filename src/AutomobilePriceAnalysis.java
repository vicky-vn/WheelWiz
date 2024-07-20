import java.util.Scanner;

public class AutomobilePriceAnalysis {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String firstName, lastName, emailID;

        int maxBudget, mobileNumber;

        FancyASCII.asciiPrint();

        PrintStatements.statementCall(PrintStatements.welcomeMsg);

        PrintStatements.statementCall(PrintStatements.firstNameRequest);
        firstName = input.nextLine();

        PrintStatements.statementCall(PrintStatements.lastNameRequest);
        lastName = input.nextLine();

        PrintStatements.statementCall(PrintStatements.contactDetailsRequest);
        // Extract the mail and number
        // Validate the mail and number

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
