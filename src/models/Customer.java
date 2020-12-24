package models;

import controllers.UpdateCustomerViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import utils.DBQuery;

import java.time.ZonedDateTime;
import java.util.Comparator;

public class Customer {

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static SortedList<Customer> customerSortedList = new SortedList<>(customerList, Comparator.comparing(Customer::getCustomerName));

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionID;
    private Division division;
    private String formattedAddress;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructor -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
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
        this.formattedAddress = this.address + ", " + this.division.getDivisionName() + ", " + this.division.getCountry().getCountryName() + " " + this.postalCode;
        System.out.println(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // INSTANCE METHODS ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

        // -------------------------------------------------------------------------------------------------------------
        // INSTANCE GETTERS --------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------

    /**
     * Returns the Customer object's name
     * @return  customerName as String
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns the Customer object's ID
     * @return  customerID as int
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Returns the Customer object's formatted address.
     * @return  formattedAddress as String
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * Returns the Customer object's phone number.
     * @return  phoneNumber as String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the Customer object's unformatted address.
     * @return  address as String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the Customer object's postal code.
     * @return  postalCode as String
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the Customer object's division.
     * @return  division as Division
     */
    public Division getDivision() {
        return division;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC METHODS --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

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
     * Removes the customer from the customerList and calls DBQuery method to remove the customer from the
     * Database.
     * @param customer The customer to be removed
     * @return true if the customer was removed, otherwise false.
     */
    public static boolean removeCustomer(Customer customer){
        if(Appointment.exists(customer)){
            System.out.println("Customer has related appointment.");
            return false;
        }
        if(DBQuery.removeCustomer(customer)){
            if(customerList.remove(customer)){
                System.out.println("The customer was successfully removed from the customerList");
                return true;
            }
        } else {
            System.out.println("Error ( Customer.removeCustomer() ) : The customer was not removed from the customerList");
            return false;
        }
        return false;
    }

    /**
     * Overrides the current customer in the customerList with the most recent customer in the database that has
     * the same customerId
     * @param customer Customer to be refreshed.
     * @return true
     */
    public static boolean refreshCustomer(Customer customer){
        System.out.println("Reloading Customer.");
        int index = customerList.indexOf(customer);
        Customer refreshedCustomer = DBQuery.loadCustomer(customer.getCustomerID());
        customerList.set(index, refreshedCustomer);
        UpdateCustomerViewController.setCustomer(refreshedCustomer);
        return true;
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

        // -------------------------------------------------------------------------------------------------------------
        // STATIC GETTERS ----------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------

    /**
     * Provides the sorted customer list.
     * @return  the Customer list sorted by customer name in ascending order.
     */
    public static SortedList<Customer> getCustomerSortedList() {
        return customerSortedList;
    }



    // Standard Overrides ---------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return customerName;
    }
}
