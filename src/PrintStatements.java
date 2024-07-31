public class PrintStatements {

    static String welcomeMsg = "\nWelcome to our application! We will help you find the right wheels\n";

    static String welcomeMsg2 = "\nPlease provide the following details to get started\n";

    static String firstNameRequest = "Please enter your first name";

    static String lastNameRequest = "Please enter your last name";

    static String validNameRequest = "Please enter a valid name";

    static String contactDetailsRequest = "\nPlease enter your E-mail ID & Mobile Number";

    static String invalidEmail = "No valid email found in the input";

    static String invalidPhone = "No valid phone number found in the input";

    static String maxBudgetRequest = "\nPlease enter your maximum budget";

    static String validBudgetRequest = "Please enter a valid price ";

    static String modelRequest = "Enter a model name to see relevant links or press 0 to exit";

    static String lastStatement = "Press 1 to analyze again\nPress 0 to exit";

    static String invalidEntry = "Invalid Entry";

    static String farewellStatement = "Thanks for using our application, Keep cruising for latest car details";


    //Method to print the default statements
    public static void statementCall(String statement) {
        System.out.println(statement);
    }
}
