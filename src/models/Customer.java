package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.ZonedDateTime;

public class Customer {

    // Static Variables -----------------------------------------------------------------------------------------------
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    // Instance Variables ---------------------------------------------------------------------------------------------
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionID;
    private Division division;

    // Constructor ----------------------------------------------------------------------------------------------------
    public Customer(){}
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;

        // Relationship Mapping
        this.division = Division.findById(this.divisionID);

        System.out.println(this);
    }

    // Instance Methods -----------------------------------------------------------------------------------------------


    // Static Methods -------------------------------------------------------------------------------------------------
    /**
     * Takes a Customer object as an input and adds it to the customerList. This is required to keep the object
     * persistent and to use the object with methods that utilize the Observablelist.
     * @param   customer    The Customer object to be added to the list.
     * @return              true if the Customer object was added, false if not.
     */
    public static boolean addToList(Customer customer){
        return customerList.add(customer);
    }

    /**
     * Prints the String representation of each Customer object in the customerList.
     */
    public static void printCustomerList(){
        System.out.println("-----------------------------------Customer List-----------------------------------");
        for(Customer customer : customerList){
            System.out.println(customer);
        }
    }

    /**
     * Searches for and returns a Customer object that has a specific customer ID.
     * @param   customerID   int of the customer ID to be searched
     * @return              The Customer object if found. Otherwise, null.
     */
    public static Customer findById(int customerID){
        for(Customer customer : customerList){
            if(customer.customerID == customerID)
                return customer;
        }
        return null;
    }

    // GETTERS --------------------------------------------------------------------------------------------------------
    /**
     * Returns the Customer object's name
     * @return  customerName as String
     */
    public String getCustomerName() {
        return customerName;
    }

    // Standard Overrides ---------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", divisionID=" + divisionID +
                ", division=" + division.getDivisionName() +
                '}';
    }
}
