package controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import models.Appointment;
import models.Contact;
import utils.LanguageHandler;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {

    ObservableList<Map.Entry<String, Integer>> typeAmounts = FXCollections.observableArrayList(Appointment.getTypeAmountMap().entrySet());
    ObservableList<Map.Entry<String, Integer>> monthAmounts = FXCollections.observableArrayList(Appointment.getMonthAmountMap().entrySet());
    ObservableList<Map.Entry<String, Integer>> customerAmounts = FXCollections.observableArrayList(Appointment.getCustomerAmountMap().entrySet());


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
    private TableView<Map.Entry<String, Integer>> typeTableView;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> typeApptAmtColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> typeAmtColumn;

    @FXML
    private TableView<Map.Entry<String, Integer>> monthlyTableView;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> monthColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> monthAmountColumn;

    @FXML
    private TableView<Map.Entry<String, Integer>> customerTableView;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> customerApptAmtColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> customerAmtColumn;

    @FXML
    private Label appointmentViewMessageLabel;

    @FXML
    private Label contactLabel;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private Label apptByTypeLabel;

    @FXML
    private Label apptByMonthLabel;

    @FXML
    private Label apptByCustomerLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactComboBox.setItems(Contact.getContactSortedList());

        contactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{

            Contact contact = newSelection;

            if(contact != null){
                Appointment.getAppointmentFilteredListByContact().setPredicate(s -> {
                    return s.getContact() == contact;
                });
            } else {
                Appointment.getAppointmentFilteredListByContact().setPredicate(s -> false);
            }
        });

        //TableView setups

        typeApptAmtColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> entryStringCellDataFeatures) {
                return new SimpleStringProperty(entryStringCellDataFeatures.getValue().getKey());
            }
        });
        typeAmtColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer> entryIntegerCellDataFeatures) {
                return new SimpleIntegerProperty(entryIntegerCellDataFeatures.getValue().getValue()).asObject();
            }
        });
        typeTableView.setItems(typeAmounts);
        typeTableView.getColumns().setAll(typeApptAmtColumn, typeAmtColumn);

        monthColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> entryStringCellDataFeatures) {
                return new SimpleStringProperty(entryStringCellDataFeatures.getValue().getKey());
            }
        });
        monthAmountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer> entryIntegerCellDataFeatures) {
                return new SimpleIntegerProperty(entryIntegerCellDataFeatures.getValue().getValue()).asObject();
            }
        });
        monthlyTableView.setItems(monthAmounts);
        monthlyTableView.getColumns().setAll(monthColumn, monthAmountColumn);

        customerApptAmtColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> entryStringCellDataFeatures) {
                return new SimpleStringProperty(entryStringCellDataFeatures.getValue().getKey());
            }
        });
        customerAmtColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Integer> entryIntegerCellDataFeatures) {
                return new SimpleIntegerProperty(entryIntegerCellDataFeatures.getValue().getValue()).asObject();
            }
        });
        customerTableView.setItems(customerAmounts);
        customerTableView.getColumns().setAll(customerApptAmtColumn, customerAmtColumn);

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

        typeApptAmtColumn.setText(LanguageHandler.getLocaleString("Type"));
        typeAmtColumn.setText(LanguageHandler.getLocaleString("Amount"));

        customerApptAmtColumn.setText(LanguageHandler.getLocaleString("Customer"));
        customerAmtColumn.setText(LanguageHandler.getLocaleString("Amount"));

        monthColumn.setText(LanguageHandler.getLocaleString("Month"));
        monthAmountColumn.setText(LanguageHandler.getLocaleString("Amount"));

        contactComboBox.setPromptText(LanguageHandler.getLocaleString("Contact"));
        contactLabel.setText(LanguageHandler.getLocaleString("Contact"));

        apptByTypeLabel.setText(LanguageHandler.getLocaleString("Appointments by type"));
        apptByMonthLabel.setText(LanguageHandler.getLocaleString("Appointments by month"));
        apptByCustomerLabel.setText(LanguageHandler.getLocaleString("Appointments by customer"));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startFormatted"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endFormatted"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentViewTable.setItems(Appointment.getAppointmentFilteredListByContact());

    }
}
