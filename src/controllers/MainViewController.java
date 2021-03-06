package controllers;

import ScheduleManager.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Appointment;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // INSTANCE VARIABLES ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC VARIABLES ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static Stage popupStage;
    private static boolean appointmentOnStartup = false;

    @FXML
    private AnchorPane anchorContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button contactButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane subAnchorContainer;

    @FXML
    private Button reportsButton;

    @FXML
    private Label mainViewLabel;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Methods ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Calls the loadAppointmentView method to show the AppointmentView.fxml in the subAnchorContainer.
     * @param event event that calls the method
     */
    @FXML
    void appointmentButtonOnClick(ActionEvent event) {
        loadAppointmentView();
        resetSelections();
    }

    /**
     * Calls the loadAppointmentView method to show the CustomerView.fxml in the subAnchorContainer.
     * @param event event that calls the method
     */
    @FXML
    void customerButtonOnClick(ActionEvent event) {
        loadCustomerView();
        resetSelections();
    }

    /**
     * Closes and exits the application
     * @param event The event that called the method.
     */
    @FXML
    void exitButtonOnClick(ActionEvent event) {
        Main.getpStage().close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Closes the stage assigned to Main.pStage and displays the LoginView.fxml
     * @param event the event that calls the method.
     */
    @FXML
    void logoutButtonOnClick(ActionEvent event) {
        loadLoginView();
        resetSelections();
    }

    @FXML
    void reportsButtonOnClick(ActionEvent event) {
        loadReportsView();
        resetSelections();
    }

    /**
     * Initializes the FXML elements with the appropriate language, labels, observableLists and listeners.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Loads the inital view (AppointmentView.fxml)
        loadAppointmentView();

        // Sets appropriate language labels
        appointmentButton.setText(LanguageHandler.getLocaleString("Appointments"));
        customerButton.setText(LanguageHandler.getLocaleString("main_Customers"));
        exitButton.setText(LanguageHandler.getLocaleString("exit"));
        logoutButton.setText(LanguageHandler.getLocaleString("Logout"));
        reportsButton.setText(LanguageHandler.getLocaleString("Reports"));

        // Mouse events to move the window.
        anchorContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });
        anchorContainer.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.getpStage().setX(mouseEvent.getScreenX() - xOffset);
                Main.getpStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        System.out.println("MainView.fxml loaded and initialized.");

        Platform.runLater(() -> {
            if(Appointment.hasUpcomingAppointment()){
                MainViewController.setAppointmentOnStartup(true);
                loadUpcomingAppointment();
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    // INSTANCE METHODS ------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Opens the LoginView.fxml in a separate stage and closes the stage assigned to Main.pStage.
     */
    private void loadLoginView(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("../views/LoginView.fxml"));
            Scene loginScene = new Scene(root);
            Stage stage = Main.getpStage();
            stage.hide();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("MainViewController loadLoginView() error.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    /**
     * Opens the AppointmentView.fxml and displays it in the subAnchorContainer anchorpane.
     */
    private void loadAppointmentView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AppointmentView.fxml"));
            Node appointmentScene = loader.load();
            subAnchorContainer.getChildren().setAll(appointmentScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens the CustomerView.fxml and displays it in the subAnchorContainer anchorpane.
     */
    private void loadCustomerView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerView.fxml"));
            Node customerScene = loader.load();
            subAnchorContainer.getChildren().setAll(customerScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens the ReportView.fxml and displays it in the subAnchorContainer anchorpane.
     */
    private void loadReportsView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ReportView.fxml"));
            Node reportsScene = loader.load();
            subAnchorContainer.getChildren().setAll(reportsScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUpcomingAppointment(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/UpcomingAppointmentView.fxml"));
            popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(Main.getpStage());
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error ( MainViewController.loadUpcomingAppointment() ): " + e.getMessage());
        }
    }

    public static Stage getPopupStage() {
        return popupStage;
    }

    public static boolean getAppointmentOnStartup(){
        return appointmentOnStartup;
    }

    public static void setAppointmentOnStartup(boolean appointmentOnStartup) {
        MainViewController.appointmentOnStartup = appointmentOnStartup;
    }

    public void resetSelections(){
        AppointmentViewController.setSelectedAppointment(null);
        CustomerViewController.setSelectedCustomer(null);
    }
}