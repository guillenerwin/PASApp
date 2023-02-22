/**
 * Java Course 4, Module 3
 * Data.java
 * 
 * Added a dedicated Data Transfer Object for all simulated database using HashMaps
 * 
 * @author Anthony Erwin Guillen
 * Created : 22 February 2023
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    static HashMap<String, ArrayList<String>> accountInfoMap = new HashMap<>(); // stores all customer account details
    static HashMap<String, ArrayList<String>> policyInfoMap = new HashMap<>(); // stores all Policy details when the Policy is bought
    static HashMap<String, ArrayList<String>> claimInfoMap = new HashMap<>(); // stores and saves all Claim numbers and details
}
