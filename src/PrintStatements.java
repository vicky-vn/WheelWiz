public class PrintStatements {


    static String welcomeMsg = "Welcome to our application! We will help you find the right wheels\n";

    static String welcomeMsg2 = "\nPlease provide the following details to get started\n";

    static String trendingCars = "Here are the trending terms available in our records!";

    static String firstNameRequest = "Please enter your first name";

    static String lastNameRequest = "Please enter your last name";

    static String contactDetailsRequest = "Please enter your E-mail ID & Mobile Number";

    static String invalidEmail = "No valid email found in the input";

    static String invalidPhone = "No valid phone number found in the input";

    static String maxBudgetRequest = "\nPlease enter your maximum budget";

    static String carCategoryRequest ="Please enter the category you're interested in";

    static String carBrandRequest = "Please enter the brand you're interested in";

    static String lastStatement = "Press 1 to analyze again\nPress 0 to exit";

    static String invalidEntry = "Invalid Entry";

    static String farewellStatement = "Thanks for using our application, Keep cruising for latest car details";


    //Method to print the default statements
    public static void statementCall(String statement) {
        System.out.println(statement);
    }
}
