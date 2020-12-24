package controllers;

import ScheduleManager.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Appointment;
import models.Contact;
import models.ConvertDate;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class AppointmentViewController implements Initializable{

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC VARIABLES ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private static Appointment selectedAppointment;
    private static Stage popupStage;
    private static boolean firstRun = true;

    @FXML
    private TableView<Appointment> appointmentViewTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, Contact> contactColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private HBox searchHBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label appointmentViewMessageLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label dateRangeLabel;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Methods ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Opens the AddAppointmentView in a separate stage. This function will set the initModality of the new stage to
     * Modality.WINDOW_MODAL and the owner will be the stage assigned to Main.pStage.
     * @param event The event that causes the method to be called.
     */
    @FXML
    void addButtonOnClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/AddAppointmentView.fxml"));
            popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(Main.getpStage());
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error ( AppointmentViewController.updateButtonOnClick() ): " + e.getMessage());
        }
    }

    /**
     * Removes the class's selectedAppointment object from the appointmentList and database table.
     * @param event event that causes the function to fire.
     */
    @FXML
    void deleteButtonOnClick(ActionEvent event) {
        if(getSelectedAppointment() == null){
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("Select an appointment to be deleted"));
            return;
        }

        String appointmentType = getSelectedAppointment().getType();
        int id = getSelectedAppointment().getAppointmentId();
        if(Appointment.removeAppointment(getSelectedAppointment())){
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("The appointment was removed successfully") + ": ID# " + id + " " + LanguageHandler.getLocaleString("Type") + ": " + appointmentType);
            setSelectedAppointment(null);
        } else {
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("There was an error with removing the selected appointment Please try again"));
        }
        appointmentViewTable.getSelectionModel().clearSelection();

    }

    /**
     * Opens the UpdateAppointmentView in a separate stage. This function will set the initModality of the new stage to
     * Modality.WINDOW_MODAL and the owner will be the stage assigned to Main.pStage.
     * @param event The event that causes the method to be called.
     */
    @FXML
    void updateButtonOnClick(ActionEvent event) {
        if(getSelectedAppointment() == null){
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("Select an appointment to be updated"));
        } else {
            appointmentViewMessageLabel.setText("");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../views/UpdateAppointmentView.fxml"));
                popupStage = new Stage();
                popupStage.initStyle(StageStyle.UNDECORATED);
                popupStage.initOwner(Main.getpStage());
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.setScene(new Scene(root));
                popupStage.show();
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Error ( AppointmentViewController.updateButtonOnClick() ): " + e.getMessage());
            }
        }
    }

    /**
     * Initializes the FXML elements with the appropriate language, labels, observableLists and listeners.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Tableview setup
            //Language Converted Buttons and Textfields
        addButton.setText(LanguageHandler.getLocaleString("Add"));
        deleteButton.setText(LanguageHandler.getLocaleString("Delete"));
        updateButton.setText(LanguageHandler.getLocaleString("Update"));
        searchTextField.setPromptText(LanguageHandler.getLocaleString("Search by Title or ID"));
        dateRangeLabel.setText(LanguageHandler.getLocaleString("Date Range"));
        endDatePicker.setPromptText(LanguageHandler.getLocaleString("End Date"));
        startDatePicker.setPromptText(LanguageHandler.getLocaleString("Start Date"));
            //Language converted column headers
        appointmentIdColumn.setText(LanguageHandler.getLocaleString("Appointment ID"));
        titleColumn.setText(LanguageHandler.getLocaleString("Title"));
        descriptionColumn.setText(LanguageHandler.getLocaleString("Description"));
        locationColumn.setText(LanguageHandler.getLocaleString("Location"));
        contactColumn.setText(LanguageHandler.getLocaleString("Contact"));
        typeColumn.setText(LanguageHandler.getLocaleString("Type"));
        startColumn.setText(LanguageHandler.getLocaleString("Start Date and TIme"));
        endColumn.setText(LanguageHandler.getLocaleString("End Date and Time"));
        customerIdColumn.setText(LanguageHandler.getLocaleString("Customer ID"));
            // Table Contents
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startFormatted"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endFormatted"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentViewTable.setItems(Appointment.getAppointmentFilteredList());

        // Date Range Filter Setup
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            AppointmentViewController.setSelectedAppointment(null);
            appointmentViewTable.getSelectionModel().clearSelection();

            ConvertDate dateTime = (date, time) -> ZonedDateTime.of(date.getValue(), LocalTime.parse(time), ZoneId.of((TimeZone.getDefault().getID()))).toInstant();
            if(startDatePicker.getValue() != null && endDatePicker.getValue() != null){
                Instant start = dateTime.toInstant(startDatePicker, "00:00");
                Instant end = dateTime.toInstant(endDatePicker, "23:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if ((s.getStart().equals(start) || s.getStart().isAfter(start)) && (s.getStart().equals(end) || s.getStart().isBefore(end))) {
                        return true;
                    }
                    return false;
                });
                return;
            }
            if(startDatePicker.getValue() != null && endDatePicker.getValue() == null){
                Instant start = dateTime.toInstant(startDatePicker, "00:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if (s.getStart().equals(start) || s.getStart().isAfter(start)) {
                        return true;
                    }
                    return false;
                });
                return;
            }

            if(startDatePicker.getValue() == null && endDatePicker.getValue() != null){
                Instant end = dateTime.toInstant(endDatePicker, "23:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if(s.getStart().equals(end) || s.getStart().isBefore(end)){
                        return true;
                    }
                    return false;
                });
                return;
            }

            Appointment.getAppointmentFilteredListByDate().setPredicate(s -> true);
        });
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            AppointmentViewController.setSelectedAppointment(null);
            appointmentViewTable.getSelectionModel().clearSelection();

            ConvertDate dateTime = (date, time) -> ZonedDateTime.of(date.getValue(), LocalTime.parse(time), ZoneId.of((TimeZone.getDefault().getID()))).toInstant();
            if(startDatePicker.getValue() != null && endDatePicker.getValue() != null){
                Instant start = dateTime.toInstant(startDatePicker, "00:00");
                Instant end = dateTime.toInstant(endDatePicker, "23:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if ((s.getStart().equals(start) || s.getStart().isAfter(start)) && (s.getStart().equals(end) || s.getStart().isBefore(end))) {
                        return true;
                    }
                    return false;
                });
                return;
            }
            if(startDatePicker.getValue() != null && endDatePicker.getValue() == null){
                Instant start = dateTime.toInstant(startDatePicker, "00:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if (s.getStart().equals(start) || s.getStart().isAfter(start)) {
                        return true;
                    }
                    return false;
                });
                return;
            }

            if(startDatePicker.getValue() == null && endDatePicker.getValue() != null){
                Instant end = dateTime.toInstant(endDatePicker, "23:00");
                Appointment.getAppointmentFilteredListByDate().setPredicate(s -> {
                    if(s.getStart().equals(end) || s.getStart().isBefore(end)){
                        return true;
                    }
                    return false;
                });
                return;
            }

            Appointment.getAppointmentFilteredListByDate().setPredicate(s -> true);
        });

        // Searchfield setup
        searchTextField.textProperty().addListener(observable ->  {
            String input = searchTextField.getText();
            setSelectedAppointment(null);

            if(input == null || input.length() == 0){

                Appointment.getAppointmentFilteredList().setPredicate(s -> true);
                appointmentViewMessageLabel.setText("");
                return;
            }

            Appointment.getAppointmentFilteredList().setPredicate( s -> {
                if(s.getTitle().toLowerCase().contains(input.toLowerCase()) || input.contains((Integer.toString(s.getAppointmentId())))){
                    appointmentViewMessageLabel.setText("");
                    return true;
                }
                return false;
            }
            );
            if(Appointment.getAppointmentFilteredList().size() == 0){
                appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("No appointments were found"));
            }
        });

        // Tableview Listener - Needed to perform functions on the selected appointment.
        appointmentViewTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, appointment, newSelection) -> setSelectedAppointment(newSelection) ));

        Platform.runLater(() -> {
            if(!MainViewController.getAppointmentOnStartup() && AppointmentViewController.firstRun){
                appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("There are no upcoming appointments"));
                setFirstRun(false);
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    // SETTERS----------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Assigns the selected appointment in the appointmentTableView to the instance's selectedAppointment variable.
     * @param appointment   The appointment to be assigned.
     */
    public static void setSelectedAppointment(Appointment appointment){
        AppointmentViewController.selectedAppointment = appointment;
        System.out.println("Selected Appointment: " + AppointmentViewController.selectedAppointment);
    }

    public static void setFirstRun(boolean firstRun) {
        AppointmentViewController.firstRun = firstRun;
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS ---------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the selected appointment in the appointment Table View.
     * @return The selected appointment in the appointment Table View.
     */
    public static Appointment getSelectedAppointment(){
        return AppointmentViewController.selectedAppointment;
    }

    /**
     * Returns the popupStage stage.
     * @return popupStage
     */
    public static Stage getPopupStage() {
        return popupStage;
    }

}