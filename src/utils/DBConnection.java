package utils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static String connectionString = null;
    private static String username = null;
    private static String password = null;
    private static Connection conn = null;

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC METHODS --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Closes open database connection if it is open.
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Database Connection Closed!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Initializes the connectionString, username, and password variables with information located in the config.properties
     * file. This function must be called before any subsequent DBConnection.startConnection() calls.
     */
    public static void setupDB() {
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileInputStream("src/utils/config.properties"));
            connectionString = dbProps.getProperty("connection_string");
            username = dbProps.getProperty("username");
            password = dbProps.getProperty("password");
            System.out.println("Database Credentials Loaded: " + connectionString + " " + username + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts and returns the connection to the database.
     * @return Connection
     */
    public static Connection startConnection() {
        try {
            conn = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Database Connection Successful!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}
