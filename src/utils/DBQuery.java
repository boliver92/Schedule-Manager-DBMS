package utils;

import models.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DBQuery {

    // READ ---------------------------------------------------------------------------------------------------------
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
    public static Appointment loadAppointment(int appointmentId){
        //Start Connection
        Connection conn = DBConnection.startConnection();


        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointments WHERE Appointment_ID = ?")){

            ps.setInt(1, appointmentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Appointment returnAppt = new Appointment(
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
                );
                DBConnection.closeConnection();
                return returnAppt;
            }

            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Close connection
        DBConnection.closeConnection();
        return null;
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

    // CREATE ----------------------------------------------------------------------------------------------------------
    public static boolean addAppointment(String title, String description, String location, String type, String start,
                                         String end, int customerId, int contactId){
        System.out.println("Database - Adding appointment");
        Connection conn = DBConnection.startConnection();
        try(PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                        "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)){


            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setTimestamp(7, Timestamp.valueOf(TimeFunctions.getUtcNowFormatted()));
            ps.setString(8, User.getUsername());
            ps.setObject(9, Timestamp.valueOf(TimeFunctions.getUtcNowFormatted()));
            ps.setString(10, User.getUsername());
            ps.setInt(11, customerId);
            ps.setInt(12, User.getUserId());
            ps.setInt(13, contactId);

            int rs = ps.executeUpdate();
            if(rs > 0){
                System.out.println("Database successfully updated.");
            }

            ResultSet resultSet = ps.getGeneratedKeys();
            if(resultSet.next()){
                Appointment.addToList(loadAppointment(resultSet.getInt(1)));
                DBConnection.closeConnection();
                return true;
            }

            if(!conn.isClosed()){
                DBConnection.closeConnection();
                return  false;
            }

            return false;

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error ( DBQuery.addAppointment() :" + e.getMessage());
            DBConnection.closeConnection();
            return false;
        }
    }
    // DELETE ----------------------------------------------------------------------------------------------------------b
    public static boolean removeAppointment(Appointment appointment){
        System.out.println("Attempting to remove appointment object from database: " + appointment);
        Connection conn = DBConnection.startConnection();

        try(PreparedStatement ps = conn.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?")){

            ps.setInt(1, appointment.getAppointmentId());

            int rs = ps.executeUpdate();
            if(rs > 0){
                System.out.println("Appointment ID# " + appointment.getAppointmentId() + " was removed successfully.");
                DBConnection.closeConnection();
                return true;
            }
            else {
                System.out.println("Alert: Appointment ID# " + appointment.getAppointmentId() + " removal was unsuccessful!.");
                DBConnection.closeConnection();
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQL Error (removeAppointment): " + e.getMessage());
            DBConnection.closeConnection();
        }
        DBConnection.closeConnection();
        return false;
    }

    // UPDATE ---------------------------------------------------------------------------------------------------------
    public static boolean updateAppointment(Appointment appointment,
                                                String title,
                                                String description,
                                                String location,
                                                String type,
                                                String start,
                                                String end,
                                                int customerId,
                                                int contactId){
        System.out.println("Database - Updating appointment: " + appointment);
        Connection conn = DBConnection.startConnection();
        try(PreparedStatement ps = conn.prepareStatement("UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?")){


            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setObject(7, Timestamp.valueOf(TimeFunctions.getUtcNowFormatted()));
            ps.setString(8, User.getUsername());
            ps.setInt(9, customerId);
            ps.setInt(10, User.getUserId());
            ps.setInt(11, contactId);
            ps.setInt(12, appointment.getAppointmentId());

            int rs = ps.executeUpdate();
            if(rs > 0){
                System.out.println("Database successfully updated.");
                DBConnection.closeConnection();
                return true;
            }

            if(!conn.isClosed()){
                DBConnection.closeConnection();
                return  false;
            }

            return false;

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error ( DBQuery.updateAppointment() :" + e.getMessage());
            DBConnection.closeConnection();
            return false;
        }


    }

}
