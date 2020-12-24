package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.*;
import utils.DBQuery;
import utils.LanguageHandler;
import views.resources.styles.Colors;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UpdateAppointmentViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static Appointment appointment;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Variables --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private HBox apptIdHBox;

    @FXML
    private TextField apptIdTextField;

    @FXML
    private TextField apptTitleTextField;

    @FXML
    private HBox searchHBox2;

    @FXML
    private TextField apptDescriptionTextField;

    @FXML
    private TextField apptLocationTextField;


    @FXML
    private TextField apptTypeTextField;


    @FXML
    private DatePicker apptStartDatePicker;

    @FXML
    private ComboBox<String> apptEndTimeComboBox;

    @FXML
    private ComboBox<String> apptStartTimeComboBox;


    @FXML
    private DatePicker apptEndDatePicker;

    @FXML
    private ComboBox<Customer> apptCustomerComboBox;

    @FXML
    private ComboBox<Contact> apptContactComboBox;

    @FXML
    private Button updateButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label uiMessageLabel;

    @FXML
    private Label updateAppUITitleLabel;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML METHODS ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Closes the window and returns to the MainView
     * @param event THe event that causes the method to be called.
     */
    @FXML
    void returnButtonOnClick(ActionEvent event) {
        AppointmentViewController.getPopupStage().close();
    }

    /**
     * Calls the validatedTextFields method and if true, it creates a replaces the current appointment object with a new one
     * in the appointmentList and updates the values in the database.
     * @param event The event that causes the method to be called
     */
    @FXML
    void updateButtonOnClick(ActionEvent event) {
        if(validatedTextFields()){

            uiMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Updating appointment information"));

            Convert dateTime = (date, time) -> ZonedDateTime.of(date.getValue(), LocalTime.parse(TimeFunctions.convertToShort(time.getValue())), ZoneId.of((TimeZone.getDefault().getID()))).toInstant();
            Instant start = dateTime.toInstant(apptStartDatePicker, apptStartTimeComboBox);
            Instant end = dateTime.toInstant(apptEndDatePicker, apptEndTimeComboBox);
            LocalDateTime startTime = LocalDateTime.ofInstant(start, ZoneOffset.UTC);
            LocalDateTime endTime = LocalDateTime.ofInstant(end, ZoneOffset.UTC);
            String startTimeFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(startTime);
            String endTimeFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endTime);

            if(DBQuery.updateAppointment(UpdateAppointmentViewController.getAppointment(),
                    apptTitleTextField.getText(),
                    apptDescriptionTextField.getText(),
                    apptLocationTextField.getText(),
                    apptTypeTextField.getText(),
                    startTimeFormatted,
                    endTimeFormatted,
                    apptCustomerComboBox.getValue().getCustomerID(),
                    apptContactComboBox.getValue().getContactID()
                    )){

                if(Appointment.refreshAppointment(appointment)){
                    uiMessageLabel.setText(LanguageHandler.getLocaleString("Update Successful"));
                }
                else {
                    uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                    uiMessageLabel.setText(LanguageHandler.getLocaleString("Unable to refresh the appointment"));
                }

            }

            else {
                uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                uiMessageLabel.setText(LanguageHandler.getLocaleString("Unable to update the database"));
            }

        }


    }

    /**
     * Initializes the view with the correct styling, listeners, and text.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UpdateAppointmentViewController.setAppointment(AppointmentViewController.getSelectedAppointment());

        apptTitleTextField.setPromptText(LanguageHandler.getLocaleString("Title"));
        apptDescriptionTextField.setPromptText(LanguageHandler.getLocaleString("Description"));
        apptLocationTextField.setPromptText(LanguageHandler.getLocaleString("Location"));
        apptTypeTextField.setPromptText(LanguageHandler.getLocaleString("Type"));
        apptCustomerComboBox.setPromptText(LanguageHandler.getLocaleString("Customer"));
        apptContactComboBox.setPromptText(LanguageHandler.getLocaleString("Contact"));
        apptStartDatePicker.setPromptText(LanguageHandler.getLocaleString("Start Date"));
        apptStartTimeComboBox.setPromptText(LanguageHandler.getLocaleString("Start Time"));
        apptEndDatePicker.setPromptText(LanguageHandler.getLocaleString("End Date"));
        apptEndTimeComboBox.setPromptText(LanguageHandler.getLocaleString("End Time"));
        updateButton.setText(LanguageHandler.getLocaleString("Update"));
        returnButton.setText(LanguageHandler.getLocaleString("Return_btn"));
        updateAppUITitleLabel.setText(LanguageHandler.getLocaleString("Update Appointment"));


        // Populating comboboxes.
        apptCustomerComboBox.setItems(Customer.getCustomerSortedList());
        apptContactComboBox.setItems(Contact.getContactSortedList());
        apptStartTimeComboBox.setItems(TimeFunctions.getTimes());
        apptEndTimeComboBox.setItems(TimeFunctions.getTimes());

        // Time object conversions
        LocalDate startDate = UpdateAppointmentViewController.getAppointment().getStart().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalDate endDate = UpdateAppointmentViewController.getAppointment().getEnd().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalTime startTime = UpdateAppointmentViewController.getAppointment().getStart().atZone(ZoneId.of("America/New_York")).toLocalTime();
        LocalTime endTime = UpdateAppointmentViewController.getAppointment().getEnd().atZone(ZoneId.of("America/New_York")).toLocalTime();
        System.out.println(startTime.toString());
        System.out.println(endTime.toString());
        System.out.println(TimeFunctions.convertToLong(startTime.toString()));
        System.out.println(TimeFunctions.convertToLong(endTime.toString()));


        //Pre-populating fields

        apptIdTextField.setText(LanguageHandler.getLocaleString("Appointment ID") + ": " + Integer.toString(UpdateAppointmentViewController.getAppointment().getAppointmentId()));
        apptTitleTextField.setText(UpdateAppointmentViewController.getAppointment().getTitle());
        apptDescriptionTextField.setText(UpdateAppointmentViewController.getAppointment().getDescription());
        apptLocationTextField.setText(UpdateAppointmentViewController.getAppointment().getLocation());
        apptTypeTextField.setText(UpdateAppointmentViewController.getAppointment().getType());
        apptCustomerComboBox.setValue(UpdateAppointmentViewController.getAppointment().getCustomer());
        apptContactComboBox.setValue(UpdateAppointmentViewController.getAppointment().getContact());
        apptStartDatePicker.setValue(startDate);
        apptEndDatePicker.setValue(endDate);
        apptStartTimeComboBox.setValue(TimeFunctions.convertToLong(startTime.toString()));
        apptEndTimeComboBox.setValue(TimeFunctions.convertToLong(endTime.toString()));

        // Screen Dragging Listeners
        containerAnchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });
        containerAnchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                AppointmentViewController.getPopupStage().setX(mouseEvent.getScreenX() - xOffset);
                AppointmentViewController.getPopupStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        // Assigns focus to a Hbox so that focus isn't on one of the text boxes on open.
        Platform.runLater(() -> {
            apptIdHBox.requestFocus();
        });

    }

    /**
     * Confirms that the text fields are filled in correctly.
     * @return True if all fields are filled in correctly, false if not.
     */
    private boolean validatedTextFields(){
        uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
        if(apptTitleTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the title"));
            return false;
        }

        if(apptDescriptionTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the description"));
            return false;
        }

        if(apptLocationTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the location"));
            return false;
        }

        if(apptTypeTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the type"));
            return false;
        }

        if(apptStartDatePicker.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select a start date"));
            return false;
        }

        if(apptStartTimeComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select a start time"));
            return false;
        }

        if(apptEndDatePicker.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select an end date"));
            return false;
        }

        if(apptEndTimeComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select an end time"));
            return false;
        }

        if(apptContactComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select a contact"));
            return false;
        }

        if(apptCustomerComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select a customer"));
            return false;
        }
        Convert dateTime = (date, time) -> ZonedDateTime.of(date.getValue(), LocalTime.parse(TimeFunctions.convertToShort(time.getValue())), ZoneId.of((TimeZone.getDefault().getID()))).toInstant();
        Instant start = dateTime.toInstant(apptStartDatePicker, apptStartTimeComboBox);
        Instant end = dateTime.toInstant(apptEndDatePicker, apptEndTimeComboBox);
        if(start.isAfter(end) || start.equals(end) || end.isBefore(start)){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please check the start and end dates and times"));
            return false;
        }

        if(Appointment.hasConflictingTime(start, end)){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("There is another appointment at this time"));
            return false;
        }

        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // SETTERS --------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Assigns UpdateAppointmentViewController.appointment to the appointment.
     * @param appointment The Appointment object to assign to UpdateAppointmentViewController.appointment
     */
    public static void setAppointment(Appointment appointment) {
        UpdateAppointmentViewController.appointment = appointment;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // GETTERS ---------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the Appointment assigned to UpdateAppointmentViewController.appointment
     * @return UpdateAppointmentViewController.appointment
     */
    public static Appointment getAppointment() {
        return appointment;
    }
}
