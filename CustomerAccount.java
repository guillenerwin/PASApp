/**
 * Java Course 4, Module 3
 * CustomerAccount.java
 * 
 * Features creating new customer accounts and assigning a 4-digit account number.
 * A customer's name is used to search for accounts containing policies and their policy.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 22 February 2023
 * 
 */
import java.util.ArrayList;

public class CustomerAccount {
    private String accountNo; // used as the key identifier for creating a policy quote
    private String firstName, lastName; // assumed customer names are unique, used to search for customer accounts
    private String address;
    
    // prompts the user for Customer Account details, saves it, and display
    public void createNewAccount() {
        try {
            ArrayList<String> loadAccountList = new ArrayList<>(); // holds new customer account details

            System.out.println();
            System.out.println("===========Create a New Account===========");

            do {
                System.out.print("Enter First Name: ");
                firstName = PASApp.scanner.nextLine();

            } while (firstName.isEmpty());

            do {
                System.out.print("Enter Last Name: ");
                lastName = PASApp.scanner.nextLine();

            } while (lastName.isEmpty());

            do {
                System.out.print("Enter Address: ");
                address = PASApp.scanner.nextLine();

            } while (address.isEmpty());

            generateAccountNo(); // new account number

            loadAccountList.add(firstName);
            loadAccountList.add(lastName);
            loadAccountList.add(address);

            Data.accountInfoMap.put(accountNo, loadAccountList); // using unique account number to account info

            System.out.println();
            System.out.println("=========Created Customer Account=========");

            System.out.println("Account #: " + accountNo);
            System.out.println("Name: " + firstName + " " + lastName);
            System.out.println("Address: " + address);

        } catch (Exception e) {
            System.out.println("Error with Customer Account creation.");
        }

    } // end of method createNewAccount

    // search and display for customer details using customer name
    public void searchAccount() {
        boolean checkAccountName = false;

        System.out.println();
        System.out.println("=====Search for Customer Account Name=====");

        try {
            do {
                System.out.print("Enter First Name: ");
                firstName = PASApp.scanner.nextLine();

            } while (firstName.isEmpty());

            do {
                System.out.print("Enter Last Name: ");
                lastName = PASApp.scanner.nextLine();

            } while (lastName.isEmpty());

            // looks through customer details
            for (String keyAccountNo : Data.accountInfoMap.keySet()) {
                ArrayList<String> accountSet = Data.accountInfoMap.get(keyAccountNo); // stores matched name customer details
                String fullName = accountSet.get(0) + " " + accountSet.get(1); // used to display customer name

                // assuming all full names are unique, use customer name to search
                if (fullName.equalsIgnoreCase(firstName + " " + lastName)) {

                    System.out.println();
                    System.out.println("Customer Account details found: ");
                    System.out.println();

                    System.out.println("Account #" + keyAccountNo);
                    System.out.println("Name: " + fullName);
                    System.out.println("Address: " + accountSet.get(2));

                    // in index 3, list all existing policies attached to the matching account
                    if (accountSet.size() > 3) {

                        String[] policySplitter = accountSet.get(3).split(", ");
                        String[] holderSplitter = accountSet.get(4).split(", ");

                        System.out.println();
                        System.out.printf("%s\t\t%s%n", "Policy Owned:", "Policy Holder Name:");

                        // displays the policy number and policy holder full name
                        for(int i = 0; i < policySplitter.length; i++) {
                            System.out.printf("%d%s%s\t%s%n", (i + 1), ". Policy #", policySplitter[i],
                                    holderSplitter[i]);
                        }

                    } else {
                        System.out.println("No Policies owned.");
                    }

                    checkAccountName = true; // matching account name found
                    break;

                } else {
                    checkAccountName = false;
                }
            }

            // exit to PAS main menu 
            if (checkAccountName == false) {
                System.out.println("No Customer Account name match.");
            }

        } catch (Exception e) {
            System.out.println("Error searching for Customer Account name.");
        }
    }

    // unique numeric 4-digit account number generator
    private void generateAccountNo() {
        int randomNumber = PASApp.random.nextInt(9999); // generating random number
        accountNo = String.format("%04d", randomNumber); // formatter to meet the 4-digit requirement i.e 0001

        try {
            // prevents duplicate account number by generating a new number
            if (Data.accountInfoMap.containsKey(accountNo)) {
                generateAccountNo();
            }

        } catch (Exception e) {
            generateAccountNo();
        }
    }
} // end class CustomerAccount
