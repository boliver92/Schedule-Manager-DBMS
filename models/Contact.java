package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contact {

    // Static Variables ----------------------------------------------------------------------------------------------
    private static ObservableList<Contact> contactList = FXCollections.observableArrayList();

    // Instance Variables --------------------------------------------------------------------------------------------
    private int contactID;
    private String contactName;
    private String contactEmail;

    // Constructor ---------------------------------------------------------------------------------------------------
    public Contact(){}
    public Contact(int contactID, String contactName, String contactEmail){
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;

        System.out.println("New Contact Object: " + this);
    }

    // Instance Methods ----------------------------------------------------------------------------------------------


    // Static Methods ------------------------------------------------------------------------------------------------
    /**
     * Takes a contact object as an input and adds it to the contactList. This is required to keep the object
     * persistent and to use the object with methods that utilize the Observablelist.
     * @param   contact The Contact object to be added to the list.
     * @return              true if the Contact object was added, false if not.
     */
    public static boolean addToList(Contact contact){
        return contactList.add(contact);
    }

    /**
     * Prints the String representation of each Contact object in the contactList.
     */
    public static void printContactList(){
        System.out.println("-----------------------------------Contact List-----------------------------------");
        for(Contact contact : contactList){
            System.out.println(contact);
        }
    }

    /**
     * Searches for and returns a Contact object that has a specific contact ID.
     * @param   contactID   int of the contact ID to be searched
     * @return              The contact object if found. Otherwise, null.
     */
    public static Contact findById(int contactID){
        for(Contact contact : contactList){
            if(contact.contactID == contactID)
                return contact;
        }
        return null;
    }


    // GETTERS --------------------------------------------------------------------------------------------------------
    /**
     * Returns the Contact object's name
     * @return  contactName as String
     */
    public String getContactName() {
        return contactName;
    }


    // Standard Overrides
    @Override
    public String toString() {
        return contactName;
    }
}
