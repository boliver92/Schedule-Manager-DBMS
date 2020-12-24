package controllers;

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
import models.Appointment;
import models.Contact;
import models.Customer;
import models.TimeFunctions;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UpcomingAppointmentViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static Appointment appointment;

    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private Label upcomingAppUITitleLabel;

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
    private Button returnButton;

    @FXML
    private Label uiMessageLabel;

    @FXML
    void returnButtonOnClick(ActionEvent event) {
        MainViewController.getPopupStage().close();
    }


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
        returnButton.setText(LanguageHandler.getLocaleString("Return_btn"));
        upcomingAppUITitleLabel.setTextFill(Color.web(Colors.WARNING.toString()));
        upcomingAppUITitleLabel.setText(LanguageHandler.getLocaleString("Upcoming Appointment"));

        // Populating comboboxes.
        apptCustomerComboBox.setItems(Customer.getCustomerSortedList());
        apptContactComboBox.setItems(Contact.getContactSortedList());
        apptStartTimeComboBox.setItems(TimeFunctions.getTimes());
        apptEndTimeComboBox.setItems(TimeFunctions.getTimes());

        // Time object conversions
        LocalDate startDate = UpcomingAppointmentViewController.getAppointment().getStart().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalDate endDate = UpcomingAppointmentViewController.getAppointment().getEnd().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate();
        LocalTime startTime = UpcomingAppointmentViewController.getAppointment().getStart().atZone(ZoneId.of("America/New_York")).toLocalTime();
        LocalTime endTime = UpcomingAppointmentViewController.getAppointment().getEnd().atZone(ZoneId.of("America/New_York")).toLocalTime();
        System.out.println(startTime.toString());
        System.out.println(endTime.toString());
        System.out.println(TimeFunctions.convertToLong(startTime.toString()));
        System.out.println(TimeFunctions.convertToLong(endTime.toString()));

        //Pre-populating fields

        apptIdTextField.setText(LanguageHandler.getLocaleString("Appointment ID") + ": " + Integer.toString(UpcomingAppointmentViewController.getAppointment().getAppointmentId()));
        apptTitleTextField.setText(UpcomingAppointmentViewController.getAppointment().getTitle());
        apptDescriptionTextField.setText(UpcomingAppointmentViewController.getAppointment().getDescription());
        apptLocationTextField.setText(UpcomingAppointmentViewController.getAppointment().getLocation());
        apptTypeTextField.setText(UpcomingAppointmentViewController.getAppointment().getType());
        apptCustomerComboBox.setValue(UpcomingAppointmentViewController.getAppointment().getCustomer());
        apptContactComboBox.setValue(UpcomingAppointmentViewController.getAppointment().getContact());
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
                MainViewController.getPopupStage().setX(mouseEvent.getScreenX() - xOffset);
                MainViewController.getPopupStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });
    }

    public static void setAppointment(Appointment appointment) {
        UpcomingAppointmentViewController.appointment = appointment;
    }

    public static Appointment getAppointment() {
        return appointment;
    }
}