package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class AddAppointmentViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Variables --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private Label addApptUITitleLabel;

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
    private HBox searchHBox4;

    @FXML
    private TextField apptTypeTextField;

    @FXML
    private HBox searchHBox41;

    @FXML
    private DatePicker apptStartDatePicker;

    @FXML
    private HBox searchHBox411;

    @FXML
    private ComboBox<String> apptStartTimeComboBox;

    @FXML
    private HBox searchHBox412;

    @FXML
    private DatePicker apptEndDatePicker;

    @FXML
    private HBox searchHBox4111;

    @FXML
    private ComboBox<String> apptEndTimeComboBox;

    @FXML
    private ComboBox<Customer> apptCustomerComboBox;

    @FXML
    private ComboBox<Contact> apptContactComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label uiMessageLabel;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML METHODS ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Calls the validatedTextFields method and if true, it creates a new appointment object and adds it to the database
     * and appointmentList
     * @param event The event that causes the method to be called
     */
    @FXML
    void addButtonOnClick(ActionEvent event) {
        if (validatedTextFields()) {
            uiMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Updating Appointment Information"));
            Convert dateTime = (date, time) -> ZonedDateTime.of(date.getValue(), LocalTime.parse(TimeFunctions.convertToShort(time.getValue())), ZoneId.of((TimeZone.getDefault().getID()))).toInstant();
            Instant start = dateTime.toInstant(apptStartDatePicker, apptStartTimeComboBox);
            Instant end = dateTime.toInstant(apptEndDatePicker, apptEndTimeComboBox);
            LocalDateTime startTime = LocalDateTime.ofInstant(start, ZoneOffset.UTC);
            LocalDateTime endTime = LocalDateTime.ofInstant(end, ZoneOffset.UTC);
            String startTimeFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(startTime);
            String endTimeFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endTime);
            System.out.println(startTimeFormatted);
            System.out.println(endTimeFormatted);

            if (DBQuery.addAppointment(apptTitleTextField.getText(), apptDescriptionTextField.getText(),
                    apptLocationTextField.getText(), apptTypeTextField.getText(), startTimeFormatted, endTimeFormatted,
                    apptCustomerComboBox.getValue().getCustomerID(), apptContactComboBox.getValue().getContactID())) {
                uiMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
                uiMessageLabel.setText("Appointment Added Successfully! ");
            }
        }
    }

    /**
     * Closes the window and returns to the MainView
     * @param event THe event that causes the method to be called.
     */
    @FXML
    void returnButtonOnClick(ActionEvent event) {
        AppointmentViewController.getPopupStage().close();
    }

    /**
     * Initializes the view with the correct styling, listeners, and text.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apptIdTextField.setPromptText(LanguageHandler.getLocaleString("Appointment ID") + " - " + LanguageHandler.getLocaleString("Disabled-auto"));
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
        addButton.setText(LanguageHandler.getLocaleString("Add"));
        returnButton.setText(LanguageHandler.getLocaleString("Return_btn"));
        addApptUITitleLabel.setText(LanguageHandler.getLocaleString("Add Appointment"));

        // Populating combobox`
        apptCustomerComboBox.setItems(Customer.getCustomerSortedList());
        apptContactComboBox.setItems(Contact.getContactSortedList());
        apptStartTimeComboBox.setItems(TimeFunctions.getTimes());
        apptEndTimeComboBox.setItems(TimeFunctions.getTimes());

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


    // -----------------------------------------------------------------------------------------------------------------
    // INSTANCE METHODS ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

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
}
