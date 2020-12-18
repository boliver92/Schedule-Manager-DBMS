package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.Appointment;
import models.Contact;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable{

    private Appointment selectedAppointment;

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

    /*
    @FXML
    void addButtonOnClick(ActionEvent event) {

    }

    @FXML
    void updateButtonOnClick(ActionEvent event) {

    }

     */

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

        if(Appointment.removeAppointment(getSelectedAppointment())){
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("The appointment was removed successfully"));
            setSelectedAppointment(null);
        } else {
            appointmentViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            appointmentViewMessageLabel.setText(LanguageHandler.getLocaleString("There was an error with removing the selected appointment Please try again"));
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
    }

    /**
     * Assigns the selected appointment in the appointmentTableView to the instance's selectedAppointment variable.
     * @param appointment   The appointment to be assigned.
     */
    private void setSelectedAppointment(Appointment appointment){
        this.selectedAppointment = appointment;
        System.out.println("Selected Appointment: " + this.selectedAppointment);
    }

    private Appointment getSelectedAppointment(){
        return this.selectedAppointment;
    }
}