/**
 * Java Course 4, Module 3
 * Policy.java
 * 
 * Contains all of Policy functions and prompts connecting to PolicyHolder, RatingEngine, Vehicle, and Claim.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 22 February 2023
 * 
 */
import java.time.LocalDate;
import java.util.ArrayList;

public class Policy {
    private String policyNo;
    private String effectiveDate;
    private String expirationDate;

    private boolean accountFound = false; // prevents Policy rating without an account
    private String checkAccountNo;        // used to compare for a matching account number
    private double policyTotalPremium;    // system derived premium cost for the Policy to be paid by the customer

    private PolicyHolder policyHolder = new PolicyHolder();           // associated Policy Holder to each Policy
    private ArrayList<String> addHolderToPolicy = new ArrayList<>();  // stores all Policy Holder details
    private ArrayList<String> addVehicleToPolicy = new ArrayList<>(); // stores all Vehicle details
    private ArrayList<String> addPolicyToAccount = new ArrayList<>(); // updates Customer Account details

    // starts the creation of Policy quote, prompts for an existing account number
    public void createPolicyQuote() {

        System.out.println();
        System.out.println("==========Create a Policy Quote===========");

        try {
            do {
                System.out.print("Enter Account #: ");
                checkAccountNo = PASApp.scanner.nextLine();

            } while (checkAccountNo.isEmpty());

            if (Data.accountInfoMap.containsKey(checkAccountNo)) {

                System.out.println("Account #" + checkAccountNo + " found. Please continue with Policy creation: ");
                System.out.println();

                // attached to matching customer account and used to list policy details
                generatePolicyNo();
                
                // prompts policy effective and calculates expiration date
                loadPolicyDate();
                
                // prompts policy holder details
                addHolder();

                // allows proceeding to Policy rating
                accountFound = true;
                setAccountFound(accountFound);                
                
            } else {
                System.out.println("No Account # match found.");

                // prevents proceeding to Policy rating
                accountFound = false;
                setAccountFound(accountFound);
            }

        } catch (Exception e) {
            System.out.println("Error with Policy quote creation.");
            
            // prevents proceeding to Policy rating
            accountFound = false;
            setAccountFound(accountFound);
        }

    } // end of method createPolicyQuote

    // prompts the user for the effective date, must be valid before proceeding to expiration date calculation
    public void loadPolicyDate() {

        try {
            do {
                System.out.print("Enter Policy Effective Date (MM/DD/YYYY): ");
                effectiveDate = PASApp.scanner.nextLine();

                if (PASApp.isEffectiveDateValid(effectiveDate) == false) {
                    effectiveDate = "";
                }

            } while (effectiveDate.isEmpty());

            // uses LocalDate, adds 6 months automatically from effective date, then back into String format
            expirationDate = LocalDate.parse(effectiveDate, PASApp.formatDate).plusMonths(6).format(PASApp.formatDate);

            System.out.println();
            System.out.println("Policy Effective Date: " + effectiveDate);
            System.out.println("Policy Expiration Date: " + expirationDate);

        } catch (Exception e) {
            loadPolicyDate();
        }
    }

    // prompts the user for all Policy Holder details, then prepare all details to be added to the policy when bought
    public void addHolder() {

        System.out.println();
        System.out.println("=========Assigning Policy Holder==========");

        // prompts for policy holder details and
        policyHolder.loadHolder();
        addHolderToPolicy = policyHolder.getLoadHolderList();
    }

    // prepares to add the vehicle details and premium charged to the policy when bought
    public void addVehicleToPolicy(ArrayList<String> loadVehicleList) {
        addVehicleToPolicy.addAll(loadVehicleList);

    }

    // displays the total premium charged for this policy and prompts the user to buy the policy
    public void askToBuyPolicy() {
        String askToBuy = "";

        System.out.println();
        try {
            do {
                System.out.printf("%s%,.02f%s", "Buy Policy with Total Premium cost of $", policyTotalPremium, " (Y/N)? ");
                askToBuy = PASApp.scanner.nextLine();

                if (askToBuy.equalsIgnoreCase("Y")) {
                    // saving the calculated total premium charged and all details
                    updatePolicyInfoMap();

                    // saving the policy number and policy holder name to the account
                    updateAccountInfoMap();

                    System.out.println();
                    System.out.printf("%s%s%n", "Successfully bought Policy #", policyNo);

                } else if (askToBuy.equalsIgnoreCase("N")) {
                    System.out.println();
                    System.out.println("Policy creation ended. Details will not be saved.");

                } else {
                    askToBuy = "";
                }

            } while (askToBuy.isEmpty()
                    && !askToBuy.equalsIgnoreCase("Y")
                    && !askToBuy.equalsIgnoreCase("N"));

        } catch (Exception e) {
            System.out.println("Invalid choice. Press Y to Buy Policy or N to end Policy quote.");
        }
    } // end of method askToBuy

    // updating, saving, and combining all Policy details to be saved and used when bought
    private void updatePolicyInfoMap() {
        ArrayList<String> policyList = new ArrayList<>(); // stores the bought Policy details to be saved

        // saving Policy date range
        policyList.add(effectiveDate);
        policyList.add(expirationDate);

        // saving Policy Holder and Vehicle details
        policyList.addAll(addHolderToPolicy);
        policyList.addAll(addVehicleToPolicy);

        // adding the calculated total premium charged at the end of the Policy
        policyList.add(Double.toString(policyTotalPremium));

        // combining all details and assigning the policy number as key
        Data.policyInfoMap.put(policyNo, policyList); 

    }

    // updates the list of owned policies from the given account number
    private void updateAccountInfoMap() {
        addPolicyToAccount = Data.accountInfoMap.get(checkAccountNo); // pulling account details

        if (addPolicyToAccount.size() == 3) {
            // adding the first policy number and policy holder name
            addPolicyToAccount.add(policyNo);
            addPolicyToAccount.add(policyHolder.getHolderFullName());

        } else {
            String addPolicyNo = addPolicyToAccount.get(3);
            String addHolderFullName = addPolicyToAccount.get(4);
            
            // adding new policy number and policy holder name into a comma separated String
            addPolicyNo += ", " + policyNo;
            addHolderFullName += ", " + policyHolder.getHolderFullName();

            // saving of newly added policy number and policy holder name
            addPolicyToAccount.set(3, addPolicyNo);
            addPolicyToAccount.set(4, addHolderFullName);

            // updating the account details with the policy number and policy holder name
            Data.accountInfoMap.put(checkAccountNo, addPolicyToAccount);
        }
    }

    // unique 6-digit numeric policy number generator
    private void generatePolicyNo() {
        int randomNumber = PASApp.random.nextInt(999999); // generating random number
        policyNo = String.format("%06d", randomNumber); // format by adding zeroes

        try {
            // prevents duplicate policy number by generating a new number
            if (Data.policyInfoMap.containsKey(policyNo)) {
                generatePolicyNo();
            }

        } catch (Exception e) {
            generatePolicyNo();
        }
    }

    // ask to cancel the policy using the date today as expiration date
    public void askToCancelPolicy() {
        String askToCancel = "";
        
        System.out.println();
        System.out.println("=============Cancel a Policy==============");
        
        try {
            do {
                System.out.print("Enter Policy #: ");
                policyNo = PASApp.scanner.nextLine();

            } while (policyNo.isEmpty());

            // matched an existing policy number
            if (Data.policyInfoMap.containsKey(policyNo)) {
                ArrayList<String> cancelPolicy = Data.policyInfoMap.get(policyNo); // used to access the matching policy details

                do {
                    System.out.print("Are you sure you want to Cancel Policy #" + policyNo + " expiring on " + cancelPolicy.get(1) + " (Y/N)? ");
                    askToCancel = PASApp.scanner.nextLine();
                    
                    if (askToCancel.equalsIgnoreCase("Y")) {
                        
                        expirationDate = PASApp.getCurrentStringDate(); // using current date as new expiration date
                        cancelPolicy.set(1, expirationDate); // saving the new expiration date
                        
                        Data.policyInfoMap.put(policyNo, cancelPolicy); // updating the policy list
                        System.out.println("Policy #" + policyNo + " is now cancelled as of " + expirationDate);
                    
                    } else if (askToCancel.equalsIgnoreCase("N")) {
                        System.out.println();
                        System.out.println("Cancellation aborted. Policy #" + policyNo + " will continue to expire on " + cancelPolicy.get(1));

                    } else {
                        System.out.println("Invalid choice. Press Y to Cancel policy or N to keep policy.");
                        askToCancel = "";
                    }
                    
                } while (askToCancel.isEmpty() 
                    && !askToCancel.equalsIgnoreCase("Y") 
                    && !askToCancel.equalsIgnoreCase("N"));
                
            } else {
                System.out.println("No Policy # match found.");
            }

        } catch (Exception e) {
            System.out.println("Error with Policy # input. No Policy # match found.");
        }
    }

    // search and display all policy details from a matching policy number
    public void searchPolicy() {

        System.out.println();
        System.out.println("===========Search for a Policy============");

        try {
            do {
                System.out.print("Enter Policy #: ");
                policyNo = PASApp.scanner.nextLine();

            } while (policyNo.isEmpty());

            // checks if the policy number exists
            if (Data.policyInfoMap.containsKey(policyNo)) {
                ArrayList<String> foundPolicy = Data.policyInfoMap.get(policyNo); // used to access the matching policy details
                String formatDisplay = "%n%s%s"; // used as format to display headers and values
                int vehicleIndex = 0; // holds the header display per vehicle in the policy

                String[] displayPolicyList = {
                    "Effective Date: ",
                    "Expiration Date: ",
                    "Policy Holder Name: ",
                    "Policy Holder Address: ",
                    "Birthdate: ",
                    "Driver's License #: ",
                    "Issue Date: "}; // 7 policy and policy holder headers

                String[] displayVehicleList = {
                    "Vehicle #",
                    "Make: ",
                    "Model: ",
                    "Year: ",
                    "Type: ",
                    "Fuel Type: ",
                    "Purchase Price: $",
                    "Color: ",
                    "Premium: $"}; // 9 vehicle headers, repeats per vehicle

                System.out.println();
                System.out.println("Policy #" + policyNo + " details found: ");

                for (int i = 0; i < foundPolicy.size(); i++) {

                    // prints next line after displaying of policy
                    if (i == 7) {
                        System.out.println();
                    }

                    if (i < 7) {
                        
                        // displaying policy dates and policy holder, stops at 7 elements
                        System.out.printf(formatDisplay, displayPolicyList[i], foundPolicy.get(i));

                    } else if (foundPolicy.size() < 16) {

                        // displaying the 1st vehicle, stops at index 16
                        System.out.printf(formatDisplay, displayVehicleList[(vehicleIndex++)], foundPolicy.get(i));

                    } else {

                        // prevents adding a new vehicle display for final index
                        if (i == (foundPolicy.size() - 1)) {

                            // displays total premium when the final index is hit
                            System.out.printf(formatDisplay, "Total Premium: $", foundPolicy.get(i));
                            System.out.println();
                            break;
                        }

                        // displays vehicles 2 or more for the matched policy
                        System.out.printf(formatDisplay, displayVehicleList[(vehicleIndex++)], foundPolicy.get(i));

                        // prints next line each vehicle
                        if (vehicleIndex == 9) {
                            System.out.println();
                            vehicleIndex = 0;
                        }
                    }
                } // end of for-loop to display foundPolicy details

            } else {
                System.out.println("No Policy # match found.");
            }

        } catch (Exception e) {
            System.out.println("Error with Policy # input.");
        }
    } // end of method searchPolicy

    // prompts to check if Claim against Policy's effective and expiration date range is valid
    public void fileClaim() {

        System.out.println();
        System.out.println("=============Accident Claim===============");

        try {
            do {
                System.out.print("Enter Policy #: ");
                policyNo = PASApp.scanner.nextLine();

            } while (policyNo.isEmpty());

            // using listed policy effective and expirations dates and convert from String to LocalDate
            LocalDate effectiveLocalDate = LocalDate.parse(Data.policyInfoMap.get(policyNo).get(0), PASApp.formatDate);
            LocalDate expirationLocalDate = LocalDate.parse(Data.policyInfoMap.get(policyNo).get(1), PASApp.formatDate);
    
            if (Data.policyInfoMap.containsKey(policyNo)) {

                // checks if policy is within date range, otherwise return to menu
                if (PASApp.isPolicyClaimable(effectiveLocalDate, expirationLocalDate) == false) {
                    return;
                }

                // initializing and proceeding to Claim against the valid matching Policy
                Claim newClaim = new Claim();
                newClaim.loadClaim(effectiveLocalDate, expirationLocalDate, policyNo);

            } else {
                System.out.println("No Policy # match found.");
            }

        } catch (Exception e) {
            System.out.println("Error filing Claim against Policy.");
        }
    }

    // passing Policy Holder details to the Rating Engine for computation
    public PolicyHolder getNewHolder() {
        return policyHolder;
    }

    // getters and setters for the calculated total premium
    public void setPolicyTotalPremium(double policyTotalPremium) {
        this.policyTotalPremium = policyTotalPremium;
    }
    public double getPolicyTotalPremium() {
        return policyTotalPremium;
    }

    // getters and setters to prevent Policy rating without an account
    public boolean isAccountFound() {
        return accountFound;
    }
    public void setAccountFound(boolean accountFound) {
        this.accountFound = accountFound;
    }


} // end class Policy.java
