/**
 * Java Course 4, Module 3
 * PASApp.java
 * 
 * An Automobile Insurance Policy Administration System (PAS) with accident Claims. 
 * Displays the main menu and handles all interaction with the PASApp ensuring the flow is always intended by having static utility methods for validations.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 31 August 2022
 * 
 */
import java.util.Scanner;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.LocalDate;

public class PASApp {
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();

    // setting date pattern of "09/29/2009", checks for valid dates, used for all date String formatting
    public static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private CustomerAccount customer = new CustomerAccount(); // used to access Customer Account functions
    private Policy policy = new Policy(); // used to access Policy functions
    private Claim claim = new Claim(); // used to access Claim functions

    // PAS main menu to be displayed every after input or choice of action
    public void displayMenu() {
        int menuChoice = 0;

        try {
            do {
                System.out.println();
                System.out.println("=======Automobile Insurance PAS Menu======");
                System.out.println("[1] Create a Customer Account");
                System.out.println("[2] Get a Policy quote and buy");
                System.out.println("[3] Cancel a Policy");
                System.out.println("[4] File an accident Claim");
                System.out.println("[5] Search for a Customer Account");
                System.out.println("[6] Search for a Policy");
                System.out.println("[7] Search for a Claim");
                System.out.println("[8] Exit");
                System.out.print("Enter menu choice: ");
                menuChoice = Integer.valueOf(scanner.nextLine());

                switch (menuChoice) {
                    case 1 -> {
                        customer.createNewAccount();
                    }
                    case 2 -> {

                        // initializing a new Policy application
                        Policy newPolicy = new Policy();
                        newPolicy.createPolicyQuote();
                        
                        // prevents continuing to rating Policy quote without an existing account
                        if (newPolicy.isAccountFound() == true) {

                            // allows the new Policy to be rated and a premium price calculated
                            RatingEngine policyRating = new RatingEngine();
                            policyRating.calculateTotalPremium(newPolicy);
                            
                            // prompts to buy or cancel the created and calculated Policy quote
                            newPolicy.askToBuyPolicy();

                        } else {
                            System.out.println("Policy quote creation failed.");
                            menuChoice = 0;
                        }
                    }
                    case 3 -> {
                        policy.askToCancelPolicy();
                    }
                    case 4 -> {
                        policy.fileClaim();
                    }
                    case 5 -> {
                        customer.searchAccount();
                    }
                    case 6 -> {     
                        policy.searchPolicy();
                    }
                    case 7 -> {
                        claim.searchClaimNo();
                    }
                    default -> {
                        break;
                    }
                }

            } while(menuChoice != 8); // pressing 8 exits PASApp
            
            System.out.println("System Exit. Thank you for using the PAS App.");
            
        } catch (Exception e) {
            System.out.println("Error with PAS menu. Please choose from [1] to [8].");
            displayMenu();
        }
        
        scanner.close();
    } // end method displayMenu

    // grabbing current LocalDate now and use the format into String
    public static String getCurrentStringDate() {
        return LocalDate.now().format(formatDate);
    }

    // grabbing the current String date and use the format into LocalDate
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.parse(LocalDate.now().format(formatDate), formatDate);

    }

    // grabbing the current year integer used for computations
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    // checks if the date input is formatted properly
    public static boolean isDateValid(String dateString) {
        try {
            LocalDate.parse(dateString, formatDate);
            return true;

        } catch (Exception e) {
            System.out.println("Invalid date format (Sample input: 09/29/2009).");
            return false;
        }
    }

    // checks if policy effective date input is valid, must be set today or after the current date only.
    // UPDATE: user may now choose any effective date.
    public static boolean isEffectiveDateValid(String effectiveDate) {
        try {
            LocalDate.parse(effectiveDate, formatDate);
            return true;

        } catch (Exception e) {
            System.out.println("Invalid effective date format.");
            return false;
        }
    }

    // checks if birthdate is valid, must be born before the current date only
    public static boolean isBirthdateValid(String birthdate) {
        try {
            if (LocalDate.parse(birthdate, formatDate).compareTo(getCurrentLocalDate()) > 0) {
                System.out.println("Invalid birthdate. Cannot be born in the future.");
                return false;

            } else {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Invalid birthdate.");
            return false;
        }
    }

    // checks if driver's license issue date is valid, must be issued today or before the current date only
    public static boolean isLicenseIssueDateValid(String licenseIssueDate) {
        try {
            if (LocalDate.parse(licenseIssueDate, formatDate).compareTo(getCurrentLocalDate()) > 0) {
                System.out.println("Invalid Driver's License issue date. Cannot be issued in the future.");
                return false;

            } else {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Invalid Driver's License issue date.");
            return false;
        }
    }

    /**
     * checks if claim date is valid, must not be after the current date, must not be out of policy date range
     * @param effectiveLocalDate from String converted into LocalDate
     * @param expirationLocalDate from String converted into LocalDate
     * @param accidentDate from user String input
     */
    public static boolean isClaimDateValid(LocalDate effectiveLocalDate, LocalDate expirationLocalDate, String accidentDate) {
        try {
            LocalDate checkAccidentDate = LocalDate.parse(accidentDate, formatDate);

            if (checkAccidentDate.compareTo(getCurrentLocalDate()) > 0) {
                // prevents claiming in the future
                System.out.println("Cannot claim future dates.");
                return false;

            } else if (checkAccidentDate.compareTo(effectiveLocalDate) >= 0 
                && checkAccidentDate.compareTo(expirationLocalDate) < 0) {
                // only accept dates within Policy range
                return true;
            
            } else {
                // displays valid coverage for claim
                System.out.println("Out of Policy coverage: " + effectiveLocalDate.format(formatDate) + " - " + expirationLocalDate.format(formatDate));
                return false;
            }

        } catch (Exception e) {
            System.out.println("Invalid claim date format.");
            return false;
        }
    }

    /**
     * prevents entering claim details if Policy coverage is not yet effective or is expired/cancelled
     * @param effectiveLocalDate from String converted into LocalDate, used to check if Claim is valid
     * @param expirationLocalDate from String converted into LocalDate, used to check if Claim is valid
     */
    public static boolean isPolicyClaimable(LocalDate effectiveLocalDate, LocalDate expirationLocalDate) {
        try {
            if (expirationLocalDate.compareTo(getCurrentLocalDate()) <= 0) {
                System.out.println("Cannot file a Claim. Policy coverage expired.");
                return false;
                
            } else {
                if (effectiveLocalDate.compareTo(getCurrentLocalDate()) > 0) {
                    System.out.println("Cannot file a Claim. Policy coverage not started yet.");
                    return false;
    
                } else {
                    // only proceeds when policy coverage is active and within 6 month range of effectivity
                    System.out.println("Policy is claimable. Please continue with Claim application: ");
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Invalid claim date against policy.");
            return false;
        }
    }
} // end class PASApp
 