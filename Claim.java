/**
 * Java Course 4, Module 3
 * Claim.java
 * 
 * Contains and handles all display and prompting of claim details. Proceeds to search for an accident claim against a matching policy number.
 * 
 * @author Anthony Erwin Guillen
 * Created : 17 August 2022
 * Modified : 22 February 2023
 * 
 * 
 */
import java.time.LocalDate;
import java.util.ArrayList;

public class Claim {
    private String claimNo;
    private String accidentDate;
    private String accidentAddress;
    private String description;
    private String damage;
    private double cost;
    private ArrayList<String> loadClaimList = new ArrayList<String>(); // used to store claim details from user input

    /**
     * prompts and validations of all claim details from user input
     * @param effectiveLocalDate used to validate accident date
     * @param expirationLocalDate used to validate accident date
     * @param policyNo used to display valid Claim against existing policy number
     */
    public void loadClaim(LocalDate effectiveLocalDate, LocalDate expirationLocalDate, String policyNo) {

        try {
            System.out.println();
            System.out.println("Claim against Policy #" + policyNo);
        
            do {
                System.out.print("Date of accident: ");
                accidentDate = PASApp.scanner.nextLine();

                if (PASApp.isClaimDateValid(effectiveLocalDate, expirationLocalDate, accidentDate) == false) {
                    accidentDate = "";
                }

            } while(accidentDate.isEmpty());

            do {
                System.out.print("Address of accident: ");
                accidentAddress = PASApp.scanner.nextLine();

            } while (accidentAddress.isEmpty());

            do {
                System.out.print("Description of accident: ");
                description = PASApp.scanner.nextLine();

            } while (description.isEmpty());

            do {
                System.out.print("Damage to vehicle: ");
                damage = PASApp.scanner.nextLine();

            } while (damage.isEmpty());

            do {
                try {
                    System.out.print("Estimated cost of repairs: $");
                    cost = Double.valueOf(PASApp.scanner.nextLine());

                } catch (Exception e) {
                    System.out.println("Invalid cost input. Please enter a number.");
                    cost = 0;
                }

            } while(cost <= 0);

            generateClaimNo();

            loadClaimList.add(policyNo);
            loadClaimList.add(accidentDate);
            loadClaimList.add(accidentAddress);
            loadClaimList.add(description);
            loadClaimList.add(damage);
            loadClaimList.add(Double.toString(cost));

            Data.claimInfoMap.put(claimNo, loadClaimList);

            System.out.println();
            System.out.println("===========Filed Accident Claim===========");
            
            System.out.println("Claim #: " + claimNo);
            System.out.println("Against Policy #: " + policyNo);
            System.out.println("Date of accident: " + accidentDate);
            System.out.println("Place of accident: " + accidentAddress);
            System.out.println("Description: " + description);
            System.out.println("Damage to vehicle: " + damage);
            System.out.printf("%s%,.02f%n","Estimated cost of repairs: $", cost);

        } catch (Exception e) {
            System.out.println("Error loading Claim details.");
            loadClaim(effectiveLocalDate, expirationLocalDate, policyNo);
        }
    } // end of method loadClaim

    // prompts for an existing Claim number to be displayed
    public void searchClaimNo() {
        
        System.out.println();
        System.out.println("=========Search for Accident Claim========");

        try {
            do {
                System.out.print("Enter Claim # (Sample input: C12345): ");
                claimNo = PASApp.scanner.nextLine();

            } while (claimNo.isEmpty());

            claimNo.toUpperCase();

            // checks for an existing policy number and display
            if (Data.claimInfoMap.containsKey(claimNo)) {
                ArrayList<String> foundClaim = Data.claimInfoMap.get(claimNo);

                System.out.println();
                System.out.println("Claim #" + claimNo + " against Policy #" + foundClaim.get(0) + " details found: ");

                System.out.println();
                System.out.println("Date of Accident: " + foundClaim.get(1));
                System.out.println("Place of Accident: " + foundClaim.get(2));
                System.out.println("Description: " + foundClaim.get(3));
                System.out.println("Damage to Vehicle: " + foundClaim.get(4));
                System.out.println("Estimated Cost of Repairs: $" + foundClaim.get(5));

            } else {
                System.out.println("No Claim # match found.");
            }

        } catch (Exception e) { 
            System.out.println("Error with Claim # (Sample input: C12345).");
        }

    }

    // assign a 6-digit alphanumeric in the following format “Cxxxxx”
    private void generateClaimNo() {
        int randomNumber = PASApp.random.nextInt(99999); // generating random number
        claimNo = "C" + String.format("%05d", randomNumber); // format by adding zeroes

        try {
            // prevents duplicate claim number by generating a new number
            if (Data.claimInfoMap.containsKey(claimNo)) {
                generateClaimNo();
            }

        } catch (Exception e) {
            generateClaimNo();
        }
    }

} // end class Claim
