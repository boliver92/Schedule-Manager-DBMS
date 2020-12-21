package controllers;

import ScheduleManager.Main;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Appointment;
import models.Customer;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // STATIC VARIABLES ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private static Customer selectedCustomer;
    public static Stage popupStage;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML VARIABLES ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    @FXML
    private TableView<Customer> customerViewTable;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;

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
    private Label customerViewMessageLabel;

    /**
     * Opens the AddCustomerView in a separate stage. This function will set the initModality of the new stage to
     * Modality.WINDOW_MODAL and the owner will be the stage assigned to Main.pStage.
     * @param event The event that causes the method to be called.
     */
    @FXML
    void addButtonOnClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/AddCustomerView.fxml"));
            popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(Main.getpStage());
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error ( CustomerViewController.addButtonOnClick() ): " + e.getMessage());
        }
    }

    /**
     * Removes the CustomerViewController.selectedCustomer object from the customerList and database customers table.
     * @param event event that causes the function to fire.
     */
    @FXML
    void deleteButtonOnClick(ActionEvent event) {
        if (CustomerViewController.getSelectedCustomer() == null) {
            customerViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            customerViewMessageLabel.setText(LanguageHandler.getLocaleString("Select a customer to be deleted"));
            return;
        }


        if (!Appointment.exists(CustomerViewController.getSelectedCustomer())){
            if (Customer.removeCustomer(CustomerViewController.getSelectedCustomer())) {
                customerViewMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
                customerViewMessageLabel.setText(LanguageHandler.getLocaleString("The customer was removed successfully"));
                CustomerViewController.setSelectedCustomer(null);
            } else {
                customerViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
                customerViewMessageLabel.setText(LanguageHandler.getLocaleString("There was an error with removing the selected customer Please try again"));
            }
        } else {
            System.out.println("Err");
            customerViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            customerViewMessageLabel.setText(LanguageHandler.getLocaleString("There was an error with removing the selected customer Please try again"));
        }


        customerViewTable.getSelectionModel().clearSelection();
    }

    /**
     * Opens the UpdateCustomerView in a separate stage. This function will set the initModality of the new stage to
     * Modality.WINDOW_MODAL and the owner will be the stage assigned to Main.pStage.
     * @param event The event that causes the method to be called.
     */
    @FXML
    void updateButtonOnClick(ActionEvent event) {
        if(CustomerViewController.getSelectedCustomer() == null){
            customerViewMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            customerViewMessageLabel.setText(LanguageHandler.getLocaleString("Select a customer to be updated"));
        } else {
            customerViewMessageLabel.setText("");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../views/UpdateCustomerView.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Tableview setup

            //Language Converted Buttons and Textfields
        addButton.setText(LanguageHandler.getLocaleString("Add"));
        deleteButton.setText(LanguageHandler.getLocaleString("Delete"));
        updateButton.setText(LanguageHandler.getLocaleString("Update"));
        searchTextField.setPromptText(LanguageHandler.getLocaleString("Search by Title or ID"));

            //Language Converted Column Headers
        customerIdColumn.setText(LanguageHandler.getLocaleString("Customer ID"));
        nameColumn.setText(LanguageHandler.getLocaleString("Name"));
        addressColumn.setText(LanguageHandler.getLocaleString("Address"));
        phoneNumberColumn.setText(LanguageHandler.getLocaleString("Phone Number"));

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAddress"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerViewTable.setItems(Customer.getCustomerSortedList());

        customerViewTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, newSelection) -> setSelectedCustomer(newSelection));
    }

    //------------------------------------------------------------------------------------------------------------------
    // SETTERS----------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Assigns the selected customer in the customerTableView to the CustomerViewController.selectedCustomer variable.
     * @param selectedCustomer   The appointment to be assigned.
     */
    public static void setSelectedCustomer(Customer selectedCustomer) {
        CustomerViewController.selectedCustomer = selectedCustomer;
        System.out.println("Selected Customer: " + CustomerViewController.selectedCustomer);
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS ---------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the selected customer in the customer Table View.
     * @return The selected customer in the customer Table View.
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Returns the popupStage
     * @return popupStage
     */
    public static Stage getPopupStage() {
        return popupStage;
    }
}
