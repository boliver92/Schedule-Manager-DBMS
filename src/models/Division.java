package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Division {

    // Static Variables -----------------------------------------------------------------------------------------------
    private static ObservableList<Division> divisionList = FXCollections.observableArrayList();
    private static FilteredList<Division> divisionFilteredList = new FilteredList<>(divisionList, s -> false);

    // Instance Variables ---------------------------------------------------------------------------------------------
    private int divisionId;
    private String divisionName;
    private int countryId;
    private Country country;

    // Constructor ----------------------------------------------------------------------------------------------------
    public Division(){}
    public Division(int divisionId, String divisionName, int countryId){
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;

        // Relationship Mapping
        this.country = Country.findById(this.countryId);

        System.out.println("New Division Object: " + this);
    }

    // Instance Methods -----------------------------------------------------------------------------------------------

    // Static Methods -------------------------------------------------------------------------------------------------
    /**
     * Takes a Division object as an input and adds it to the divisionList. This is required to keep the object
     * persistent and to use the object with methods that utilize the Observablelist.
     * @param   division    The Division object to be added to the list.
     * @return              true if the Division object was added, false if not.
     */
    public static boolean addToList(Division division){
        return divisionList.add(division);
    }
    /**
     * Prints the String representation of each Division object in the divisionList.
     */
    public static void printDivisionList(){
        System.out.println("-----------------------------------Division List-----------------------------------");
        for(Division division : divisionList){
            System.out.println(division);
        }
    }
    /**
     * Searches for and returns a Division object that has a specific division ID.
     * @param   divisionId  int of the division ID to be searched
     * @return              The Division object if found. Otherwise, null.
     */
    public static Division findById(int divisionId){
        for(Division division : divisionList){
            if(division.divisionId == divisionId)
                return division;
        }
        return null;
    }

    // GETTERS --------------------------------------------------------------------------------------------------------
    /**
     * Returns the Division object's name
     * @return  divisionName as String
     */
    public String getDivisionName() {
        return divisionName;
    }

    public Country getCountry() {
        return country;
    }

    public static FilteredList<Division> getDivisionFilteredList() {
        return divisionFilteredList;
    }

    public int getDivisionId() {
        return divisionId;
    }

    // Standard Overrides ---------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return getDivisionName();
    }
}
