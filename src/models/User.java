package models;

import utils.LanguageHandler;

import java.time.ZoneId;
import java.util.Locale;

public class User {

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static int userId;
    private static String username;
    private static final ZoneId userZone = ZoneId.systemDefault();
    private static String userLocation;

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC METHODS --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

        // -------------------------------------------------------------------------------------------------------------
        // STATIC GETTERS ----------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------

    /**
     * Returns the username of the current User
     * @return username as String
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Returns the userId of the current User
     * @return userId as int
     */
    public static int getUserId() {
        return userId;
    }

    /**
     * Returns the location of the current User
     * @return location as String
     */
    public static String getUserLocation() {
        return userLocation;
    }

        // -------------------------------------------------------------------------------------------------------------
        // STATIC SETTERS ----------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------

    /**
     * Sets the userId and username for the current application user
     * @param userId the userId to set
     * @param username the username to set
     */
    public static void setUser(int userId, String username){
        User.username = username;
        User.userId = userId;

        System.out.println("New User Assigned! Username: " + username + " ID# " + userId + " Zone: " + userZone);
    }

    /**
     * Sets the userLocation based on the system's default timezone.
     */
    public static void setUserLocation() {
        if(userZone.equals(ZoneId.of("America/Phoenix"))){
            userLocation = "Phoenix, Arizona";
            System.out.println("Detected ZoneId: " + userZone + ". Setting user location as: " + userLocation + ".");
        }
        else if(userZone.equals(ZoneId.of("America/New_York"))) {
            if(LanguageHandler.getLocale().equals(Locale.FRENCH)){
                userLocation = "Montreal, Canada";
                System.out.println("Detected ZoneId: " + userZone + ". Setting user location as: " + userLocation + ".");
            }
            else {
                userLocation = "White Plains, New York";
                System.out.println("Detected ZoneId: " + userZone + ". Setting user location as: " + userLocation + ".");
            }
        }
        else if(userZone.equals(ZoneId.of("Europe/London"))){
            userLocation = "London, Europe";
            System.out.println("Detected ZoneId: " + userZone + ". Setting user location as: " + userLocation + ".");
        } else {
            userLocation = "Unknown";
            System.out.println("Detected ZoneId: " + userZone + ". Setting user location as: " + userLocation + ".");
        }
    }
}
