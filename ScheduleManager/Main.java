package ScheduleManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import utils.DBConnection;
import utils.DBQuery;
import utils.LanguageHandler;

import java.time.ZoneId;
import java.util.Locale;

public class Main extends Application {

    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/LoginView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Schedule Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        pStage = primaryStage;
    }

    public static Stage getpStage(){
        return pStage;
    }

    public static void setpStage(Stage pStage) {
        Main.pStage = pStage;
    }

    public static void main(String[] args) {
        // Load the database entries to objects.
        DBConnection.setupDB();
        DBQuery.loadCountries();
        DBQuery.loadDivisions();
        DBQuery.loadCustomers();
        DBQuery.loadContacts();
        DBQuery.loadAppointments();
        LanguageHandler.setupLanguageHandler();
        User.setUserLocation();

        // Load main FXML
        launch(args);
    }
}
