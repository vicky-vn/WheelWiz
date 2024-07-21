public class PrintStatements {

    static String trendingCars = "Here are the trending cars available in our records!";

    static String welcomeMsg = "Welcome! We will help you find the right car\nPlease provide the following details to get started\n";

    static String firstNameRequest = "Please enter your first name";

    static String lastNameRequest = "Please enter your last name";

    static String contactDetailsRequest = "Please enter your E-mail ID & Mobile Number";

    static String invalidEmail = "No valid email found in the input";

    static String invalidPhone = "No valid phone number found in the input";

    static String maxBudgetRequest = "Please enter your maximum budget";

    static String carCategoryRequest ="Please enter the category you're interested in";

    static String carBrandRequest = "Please enter the brand you're interested in";

    static String lastStatement = "Press 1 to analyze again\nPress 0 to exit";

    static String invalidEntry = "Invalid Entry";


    //Method to print the default statements
    public static void statementCall(String statement) {
        System.out.println(statement);
    }
}
