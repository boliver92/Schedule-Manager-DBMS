package models;

import utils.LanguageHandler;

import java.time.ZoneId;
import java.util.Locale;

public class User {

    private static int userId;
    private static String username;
    private static final ZoneId userZone = ZoneId.systemDefault();
    private static String userLocation;

    // GETTERS
    public static String getUsername() {
        return username;
    }
    public static int getUserId() {
        return userId;
    }

    public static ZoneId getUserZone() {
        return userZone;
    }

    public static String getUserLocation() {
        return userLocation;
    }

    // SETTERS

    public static void setUser(int userId, String username){
        User.username = username;
        User.userId = userId;

        System.out.println("New User Assigned! Username: " + username + " ID# " + userId + " Zone: " + userZone);
    }

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
