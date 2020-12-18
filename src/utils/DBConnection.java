package utils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    // JDBC URL parts
    private static String connectionString = null;
    private static String username = null;
    private static String password = null;
    private static Connection conn = null;

    public static Connection startConnection()
    {
        try{
            conn = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Database Connection Successful!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        try{
            conn.close();
            System.out.println("Database Connection Closed!");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void setupDB(){
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

    public static Connection getConnection() {
        try {
            if(conn.isClosed()){
                System.out.println("Get Database Connection Called! No connection established.");
                System.out.println("Opening a new Database Connection.");
                startConnection();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error: " + throwables.getMessage());
        }
        return conn;
    }
}
