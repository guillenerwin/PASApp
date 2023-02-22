/**
 * Java Course 4, Module 3
 * PolicyHolder.java
 * 
 * Contains all of associated Policy Holder details and for public access of the Policy Holder list for computations.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 07 September 2022
 * 
 */
import java.util.ArrayList;

public class PolicyHolder {
    private String holderFirstName;
    private String holderLastName;
    private String holderAddress;
    private String birthdate;
    private String licenseId;
    private String licenseIssueDate;

    // prompts and validations for all Policy Holder details
    public void loadHolder() {
        try {
            do {
                System.out.print("Enter First Name: ");
                holderFirstName = PASApp.scanner.nextLine();

            } while(holderFirstName.isEmpty());

            do {
                System.out.print("Enter Last Name: ");
                holderLastName = PASApp.scanner.nextLine();

            } while(holderLastName.isEmpty());

            do {
                System.out.print("Enter Address: ");
                holderAddress = PASApp.scanner.nextLine();

            } while(holderAddress.isEmpty());

            do {
                System.out.print("Enter Birthdate (MM/DD/YYYY): ");
                birthdate = PASApp.scanner.nextLine();

                // prevents birthdates in the future
                if (PASApp.isBirthdateValid(birthdate) == false) {
                    birthdate = "";
                }

            } while (birthdate.isEmpty());

            do {
                System.out.print("Enter Driver's License #: ");
                licenseId = PASApp.scanner.nextLine();

            } while(licenseId.isEmpty());

            do {
                System.out.print("Enter License Issue Date (MM/DD/YYYY): ");
                licenseIssueDate = PASApp.scanner.nextLine();

                if (PASApp.isLicenseIssueDateValid(licenseIssueDate) == false) {
                    licenseIssueDate = "";
                }

            } while (licenseIssueDate.isEmpty());

        } catch (Exception e) {
            System.out.println("Error with Policy Holder details.");
            loadHolder();
        }
    }

    // method to access and return all Policy Holder details into a list for other classes
    public ArrayList<String> getLoadHolderList() {
        ArrayList<String> loadHolderList = new ArrayList<>();

        loadHolderList.add(getHolderFullName());
        loadHolderList.add(holderAddress);
        loadHolderList.add(birthdate);
        loadHolderList.add(licenseId);
        loadHolderList.add(licenseIssueDate);

        return loadHolderList;
    }

    // getter for assigned Policy Holder name
    public String getHolderFullName() {
        return holderFirstName + " " + holderLastName;
    }

    // getter for user prompted license issue year for computation
    public int getLicenseIssueYear() {
        int licenseIssueYear = 0;
        String[] splitter = licenseIssueDate.split("/");
        
        licenseIssueYear = Integer.parseInt(splitter[2]);

        return licenseIssueYear;
    }

} // end class PolicyHolder.java
