/**
 * Java Course 4, Module 3
 * RatingEngine.java
 * 
 * Uses Policy Rating Algorithm to compute for individual car premiums and pass it on to Policy object and Vehicle.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 07 September 2022
 * 
 */
public class RatingEngine {
    private double vp = 0;  // vehicle purchase price
    private double vpf = 0; // vehicle price factor
    private int dlx = 0;    // num of years since issuance of driver's license
    private double P = 0;   // calculated premium for each vehicle
    
    // calculation of the premium based on the Policy Rating Algorithm
    public double calculateTotalPremium(Policy policy) {
        
        int vehicleAge = 0; // used to calculate for vehicle price factor (vpf) each car
        int carCount = 0; // used by Policy for car count display
        double totalPremium = 0; // prepares the total policy Premium for the Policy

        System.out.println();
        System.out.println("===========Insuring Automobile/s==========");

        do {
            try {
                System.out.print("Enter Number of Vehicle/s: ");
                carCount = Integer.valueOf(PASApp.scanner.nextLine());
                
            } catch (Exception e) {
                System.out.println("Invalid vehicle count input. Please enter a number.");
                carCount = 0;
            }

        } while (carCount <= 0);

        for (int i = 0; i < carCount; i++) {
            int displayVehicleNo = i + 1;

            System.out.println();
            System.out.println("Vehicle #" + displayVehicleNo);

            // initializing a new car each time, will only be on one policy
            Vehicle newCar = new Vehicle();
            newCar.loadVehicle();
            
            // get vehicle price using user prompted purchase price of each new car
            vp = newCar.getPurchasePrice();

            // get vehicle price factor using vehicle age and rating table
            vehicleAge = PASApp.getCurrentYear() - newCar.getVehicleYear();
            calculateVehiclePriceFactor(vehicleAge);

            // get driver's license age using the current year reduced by license issue year
            dlx = PASApp.getCurrentYear() - policy.getNewHolder().getLicenseIssueYear();

            // default license issue age to 1 for new drivers
            if (dlx == 0) {
                dlx = 1;
            }

            // Policy Rating Algorithm used to get the premium charged for each car
            P = (vp * vpf) + ((vp / 100) / dlx);
            newCar.setPremium(P);

            // passing of calculated premium for each vehicle to be added to this policy
            policy.addVehicleToPolicy(newCar.getLoadVehicleList(displayVehicleNo, newCar.getPremium())); 

            // adding of all vehicle premium charged for this Policy
            totalPremium += newCar.getPremium(); 
        }

        // setting the associated total premium to the Policy before returning
        policy.setPolicyTotalPremium(totalPremium); 

        return policy.getPolicyTotalPremium();
    }

    // uses the vehicle age to select the highest factor to match for each car
    private double calculateVehiclePriceFactor(int vehicleAge) {

        if (vehicleAge < 1) {
            vpf = .010;
        } else if (vehicleAge < 3) {
            vpf = .008;
        } else if (vehicleAge < 5) {
            vpf = .007;
        } else if (vehicleAge < 10) {
            vpf = .006;
        } else if (vehicleAge < 15) {
            vpf = .004;
        } else if (vehicleAge < 20) {
            vpf = .002;
        } else if (vehicleAge < 40) {
            vpf = .001;
        }

        return vpf;
    }

} // end class RatingEngine.java
