package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Country {

    // Static Variables -----------------------------------------------------------------------------------------------
    private static ObservableList<Country> countryList = FXCollections.observableArrayList();

    // Instance Variables ---------------------------------------------------------------------------------------------
    private int countryID;
    private String country;

    // Constructor ----------------------------------------------------------------------------------------------------
    public Country(){}
    public Country(int countryID, String country){
        this.countryID = countryID;
        this.country = country;

        System.out.println(this);
    }

    // Instance Methods -----------------------------------------------------------------------------------------------


    // Static Methods -------------------------------------------------------------------------------------------------
    /**
     * Takes a country object as an input and adds it to the countryList. This is required to keep the object
     * persistent and to use the object with methods that utilize the Observablelist.
     * @param   country     The Country object to be added to the list.
     * @return              true if the country object was added, false if not.
     */
    public static boolean addToList(Country country){
        return countryList.add(country);
    }
    /**
     * Prints the String representation of each Country object in the contactList.
     */
    public static void printCountryList(){
        System.out.println("-----------------------------------Country List-----------------------------------");
        for(Country country : countryList){
            System.out.println(country);
        }
    }
    /**
     * Searches for and returns a Country object that has a specific country ID.
     * @param   countryID   int of the country ID to be searched
     * @return              The Country object if found. Otherwise, null.
     */
    public static Country findById(int countryID){
        for(Country country : countryList){
            if(country.countryID == countryID)
                return country;
        }
        return null;
    }


    // GETTERS --------------------------------------------------------------------------------------------------------
    /**
     * Returns the Country object's name
     * @return  country as String
     */
    public String getCountryName() {
        return country;
    }

    // STANDARD OVERRIDES ---------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Country{" +
                "countryID=" + countryID +
                ", country='" + country + '\'' +
                '}';
    }
}
