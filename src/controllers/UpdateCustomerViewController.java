package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Country;
import models.Customer;
import models.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCustomerViewController implements Initializable {

    private double xOffset, yOffset;
    private Customer customer;

    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private Label updateAppUITitleLabel;

    @FXML
    private HBox apptIdHBox;

    @FXML
    private TextField IdTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private HBox searchHBox2;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button updateButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label uiMessageLabel;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<Division> divisionComboBox;

    @FXML
    private HBox searchHBox21;

    @FXML
    private TextField zipCodeTextField;

    @FXML
    private HBox searchHBox211;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    void returnButtonOnClick(ActionEvent event) {
        CustomerViewController.getPopupStage().close();
    }

    @FXML
    void updateButtonOnClick(ActionEvent event) {
        return;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customer = CustomerViewController.getSelectedCustomer();



        // Initializing comboboxes
        countryComboBox.setItems(Country.getCountryList());
        countryComboBox.setValue(customer.getDivision().getCountry());
        divisionComboBox.setItems(Division.getDivisionFilteredList());
        divisionComboBox.setValue(customer.getDivision());


        // Pre-populating fields
        IdTextField.setText("Customer ID: " + Integer.toString(customer.getCustomerID()));
        addressTextField.setText(customer.getAddress());
        nameTextField.setText(customer.getCustomerName());
        zipCodeTextField.setText(customer.getPostalCode());
        phoneNumberTextField.setText(customer.getPhoneNumber());

        countryComboBox.selectionModelProperty().addListener(observable -> {
            Country selection = countryComboBox.getValue();

            if(selection == null){
                Division.getDivisionFilteredList().setPredicate(s -> false);
                return;
            }

            Division.getDivisionFilteredList().setPredicate(s -> {
                if(s.getCountry().getCountryID() == selection.getCountryID()){
                    return true;
                }
                return false;
            });
        });

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
                CustomerViewController.getPopupStage().setX(mouseEvent.getScreenX() - xOffset);
                CustomerViewController.getPopupStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });


    }
}
