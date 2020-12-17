package utils;

import models.*;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DBQuery {

    // LOADERS ---------------------------------------------------------------------------------------------------------
    public static void loadAppointments(){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointments")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Appointment.addToList(
                    new Appointment(
                            rs.getInt("Appointment_ID"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Location"),
                            rs.getString("Type"),
                            rs.getTimestamp("Start").toLocalDateTime().toInstant(ZoneOffset.ofHours(0)),
                            rs.getTimestamp("End").toLocalDateTime().toInstant(ZoneOffset.ofHours(0)),
                            rs.getInt("Customer_ID"),
                            rs.getInt("Contact_ID"),
                            rs.getInt("User_ID")
                    )
                );
            }
            Appointment.printAppointmentList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
    }
    /**
     * Loads the entries in the contacts table as Contact objects.
     */
    public static void loadContacts(){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM contacts")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Contact.addToList(
                    new Contact(
                            rs.getInt("Contact_ID"),
                            rs.getString("Contact_Name"),
                            rs.getString("Email")
                    )
                );
            }
            Contact.printContactList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
    }
    /**
     * Loads the entries in the countries table as Country objects.
     */
    public static void loadCountries(){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM countries")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Country.addToList(
                    new Country(
                            rs.getInt("Country_ID"),
                            rs.getString("Country")
                    )
                );
            }
            Country.printCountryList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
    }
    /**
     * Loads the entries in the Customer table as Customer objects.
     */
    public static void loadCustomers(){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Customer.addToList(
                    new Customer(
                      rs.getInt("Customer_ID"),
                      rs.getString("Customer_Name"),
                      rs.getString("Address"),
                      rs.getString("Postal_Code"),
                      rs.getString("Phone"),
                      rs.getInt("Division_ID")
                    )
                );
            }
            Customer.printCustomerList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
    }
    /**
     * Loads the entries in the first_level_divisions table as Division objects.
     */
    public static void loadDivisions(){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM first_level_divisions")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Division.addToList(
                    new Division(
                            rs.getInt("Division_ID"),
                            rs.getString("Division"),
                            rs.getInt("COUNTRY_ID")
                    )
                );
            }
            Division.printDivisionList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
    }

    // LOGIN VALIDATION ------------------------------------------------------------------------------------------------
    public static boolean validateLogin(String username, String password){
        // Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?")){
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User.setUser(rs.getInt("User_ID"), rs.getString("User_Name"));
                DBConnection.closeConnection();
                Logger.logLogin(username, true);
                return true;
            }
            else {
                DBConnection.closeConnection();
                Logger.logLogin(username, false);
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        DBConnection.closeConnection();
        Logger.logLogin(username, false);
        return false;
    }

}
