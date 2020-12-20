package controllers;

import ScheduleManager.Main;
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
import models.Appointment;
import models.Contact;
import models.Customer;
import models.TimeFunctions;
import utils.DBQuery;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UpdateAppointmentViewController implements Initializable {

    private double xOffset, yOffset;
    private Appointment appointment;

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
    void returnButtonOnClick(ActionEvent event) {
        AppointmentViewController.getPopupStage().close();
    }

    @FXML
    void updateButtonOnClick(ActionEvent event) {
        if(validatedTextFields()){
            uiMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
            uiMessageLabel.setText("Updating Appointment Information. . . ");
            // TODO: Update appointment object... By replacement or setters?
            if(DBQuery.updateAppointment(appointment,
                    apptTitleTextField.getText(),
                    apptDescriptionTextField.getText(),
                    apptLocationTextField.getText(),
                    apptTypeTextField.getText(),
                    Instant.now(),
                    Instant.now(),
                    apptCustomerComboBox.getValue().getCustomerID(),
                    apptContactComboBox.getValue().getContactID()
                    )){

                if(Appointment.refreshAppointment(appointment)){
                    uiMessageLabel.setText("Update successful!");
                }
                else {
                    uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                    uiMessageLabel.setText("Unable to refresh the appointment.");
                }

            }
            else {
                uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                uiMessageLabel.setText("Unable to update the database..");
            }

        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.appointment = AppointmentViewController.getSelectedAppointment();
        // Populating comboboxes.
        apptCustomerComboBox.setItems(Customer.getCustomerSortedList());
        apptContactComboBox.setItems(Contact.getContactSortedList());
        apptStartTimeComboBox.setItems(TimeFunctions.getTimes());
        apptEndTimeComboBox.setItems(TimeFunctions.getTimes());

        // Time object conversions
        LocalDate startDate = appointment.getStart().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalDate endDate = appointment.getEnd().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalTime startTime = appointment.getStart().atZone(ZoneId.of("America/New_York")).toLocalTime();
        LocalTime endTime = appointment.getEnd().atZone(ZoneId.of("America/New_York")).toLocalTime();
        System.out.println(startTime.toString());
        System.out.println(endTime.toString());
        System.out.println(TimeFunctions.convertToLong(startTime.toString()));
        System.out.println(TimeFunctions.convertToLong(endTime.toString()));


        //Pre-populating fields

        apptIdTextField.setText(Integer.toString(appointment.getAppointmentId()));
        apptTitleTextField.setText(appointment.getTitle());
        apptDescriptionTextField.setText(appointment.getDescription());
        apptLocationTextField.setText(appointment.getLocation());
        apptTypeTextField.setText(appointment.getType());
        apptCustomerComboBox.setValue(appointment.getCustomer());
        apptContactComboBox.setValue(appointment.getContact());
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

        return true;
    }
}
