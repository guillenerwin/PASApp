/**
 * Java Course 4, Module 3
 * Vehicle.java
 * 
 * Contains all of Vehicle details, prompts, and saving or passing of details to RatingEngine and Policy.
 * 
 * @author Anthony Erwin Guillen
 * Created : 23 June 2022
 * Modified : 07 September 2022
 * 
 */
import java.util.ArrayList;

public class Vehicle extends Policy {
    private String make;
    private String model;
    private int vehicleYear = 0;
    private String type;
    private String fuel;
    private double purchasePrice = 0;
    private String color;
    private double premium = 0;

    private ArrayList<String> loadVehicleList = new ArrayList<String>(); // stores Vehicle details from user inputs

    // prompts for vehicle associated information
    public void loadVehicle() {

        // vehicle details options list, index 0 is used to prevent empty ("") inputs
        String[] makeList = {"", "Toyota", "Ford", "BMW", "Other"};
        String[] typeList = {"", "4-door Sedan", "2-door Sports Car", "SUV", "Truck"};
        String[] fuelList = {"", "Diesel", "Electric", "Petrol"};

        try {
            do {
                System.out.print("Choose Vehicle Make - [1] Toyota, [2] Ford, [3] BMW, [4] Other: ");
                make = PASApp.scanner.nextLine();
                
                switch (make) {
                    case "1" -> make = makeList[1];
                    case "2" -> make = makeList[2];
                    case "3" -> make = makeList[3];
                    case "4" -> make = makeList[4]; // assigns "Other" for unknown make
                    default -> make = "";
                }
    
            } while(make == makeList[0]
                && make != makeList[1] 
                && make != makeList[2] 
                && make != makeList[3]
                && make != makeList[4]);
            
            do {
                System.out.print("Enter Vehicle Model: ");
                model = PASApp.scanner.nextLine();

            } while (model.isEmpty());

            do {
                try {
                    System.out.print("Enter Vehicle Year (YYYY): ");
                    vehicleYear = Integer.valueOf(PASApp.scanner.nextLine());

                } catch (Exception e) {
                    System.out.println("Invalid vehicle year. Please enter a number.");
                    vehicleYear = 0;
                }

            } while (vehicleYear <= 999); // user must input 4-digit year numbers only

            do {
                System.out.print("Choose Vehicle Type - [1] 4-door Sedan, [2] 2-door Sports Car, [3] SUV, [4] Truck: ");
                type = PASApp.scanner.nextLine();

                switch (type) {
                    case "1" -> type = typeList[1];
                    case "2" -> type = typeList[2];
                    case "3" -> type = typeList[3];
                    case "4" -> type = typeList[4];
                    default -> type = "";
                }

            } while (type == typeList[0]
                && type != typeList[1] 
                && type != typeList[2] 
                && type != typeList[3]
                && type != typeList[4]);

            do {
                System.out.print("Choose Fuel Type - [1] Diesel, [2] Electric, [3] Petrol: ");
                fuel = PASApp.scanner.nextLine();
                
                switch (fuel) {
                    case "1" -> fuel = fuelList[1];
                    case "2" -> fuel = fuelList[2];
                    case "3" -> fuel = fuelList[3];
                    default -> fuel = "";
                }

            } while (fuel == fuelList[0] 
                && fuel != fuelList[1] 
                && fuel != fuelList[2]
                && fuel != fuelList[3]);

            do {
                try {
                    System.out.print("Enter Purchase Price: $");
                    purchasePrice = Double.valueOf(PASApp.scanner.nextLine());

                } catch (Exception e) {
                    System.out.println("Invalid price input. Please enter a number.");
                    purchasePrice = 0;
                }

            } while (purchasePrice <= 0);

            do {
                System.out.print("Enter Vehicle Color: ");
                color = PASApp.scanner.nextLine();
                
            } while (color.isEmpty());
            
        } catch (Exception e) {
            System.out.println("Error loading vehicle details.");
        }
    } // end of method loadVehicle
    
    // adding all associated Vehicle details and getting Premium
    public ArrayList<String> getLoadVehicleList(int vehicleNo, double premium) {

        loadVehicleList.add(Integer.toString(vehicleNo));
        loadVehicleList.add(make);
        loadVehicleList.add(model);
        loadVehicleList.add(Integer.toString(vehicleYear));
        loadVehicleList.add(type);
        loadVehicleList.add(fuel);
        loadVehicleList.add(Double.toString(purchasePrice));
        loadVehicleList.add(color);
        loadVehicleList.add(Double.toString(premium));
        
        return loadVehicleList;
    }

    // getters and setters for associated Vehicle details
    public int getVehicleYear() {
        return vehicleYear;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

} // end class Vehicle
