package controllers;

import javafx.application.Platform;
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
import javafx.scene.paint.Color;
import models.Country;
import models.Division;
import utils.DBQuery;
import utils.LanguageHandler;
import views.resources.styles.Colors;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Variables --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private Label addCustomerUITitleLabel;

    @FXML
    private HBox customerIdHbox;

    @FXML
    private TextField IdTextField;

    @FXML
    private HBox nameHBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private HBox addressHbox;

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
    private HBox postalCodeHbox;

    @FXML
    private TextField zipCodeTextField;

    @FXML
    private HBox phoneNumberHbox;

    @FXML
    private TextField phoneNumberTextField;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML METHODS ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Closes the window and returns to the MainView
     * @param event THe event that causes the method to be called.
     */
    @FXML
    void returnButtonOnClick(ActionEvent event) {
        CustomerViewController.getPopupStage().close();
    }

    /**
     * Calls the validatedTextFields method and if true, it creates a new appointment object and replaces the currently
     * selected object in the appointmentList
     * @param event The event that causes the method to be called
     */
    @FXML
    void updateButtonOnClick(ActionEvent event) {
        if(validatedTextFields()){
            uiMessageLabel.setTextFill(Color.web(Colors.WARNING.toString()));
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Adding customer"));
            if(DBQuery.addCustomer(nameTextField.getText(), addressTextField.getText(), divisionComboBox.getValue(),
                    zipCodeTextField.getText(), phoneNumberTextField.getText())){
                uiMessageLabel.setTextFill(Color.web(Colors.SUCCESS.toString()));
                uiMessageLabel.setText(LanguageHandler.getLocaleString("Customer Added Successfully"));
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

        // Country Combo Box Listener - Needed to populate division selections on country selection.
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            Country selection = newSelection;

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

        // Language Conversions
        addCustomerUITitleLabel.setText(LanguageHandler.getLocaleString("Add Customer"));
        IdTextField.setPromptText(LanguageHandler.getLocaleString("Customer ID"));
        nameTextField.setPromptText(LanguageHandler.getLocaleString("Name"));
        addressTextField.setPromptText(LanguageHandler.getLocaleString("Address"));
        countryComboBox.setPromptText(LanguageHandler.getLocaleString("Country"));
        divisionComboBox.setPromptText(LanguageHandler.getLocaleString("Division"));
        phoneNumberTextField.setPromptText(LanguageHandler.getLocaleString("Phone Number"));
        zipCodeTextField.setPromptText(LanguageHandler.getLocaleString("Postal Code"));
        updateButton.setText(LanguageHandler.getLocaleString("Add"));
        returnButton.setText(LanguageHandler.getLocaleString("Return"));

        // Initializing comboboxes
        countryComboBox.setItems(Country.getCountryList());
        divisionComboBox.setItems(Division.getDivisionFilteredList());

        //Screen Dragging Listeners
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

        Platform.runLater(() -> {
            customerIdHbox.requestFocus();
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
        if(nameTextField.getText().isEmpty()) {
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the customer's name"));
            return false;
        }
        if(addressTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the customer's address"));
            return false;
        }
        if(countryComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select the customer's country"));
            return false;
        }
        if(divisionComboBox.getValue() == null){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please select the customer's division"));
            return false;
        }
        if(zipCodeTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the customer's postal code"));
            return false;
        }
        if(phoneNumberTextField.getText().isEmpty()){
            uiMessageLabel.setText(LanguageHandler.getLocaleString("Please fill in the customer's phone number"));
            return false;
        }
        return true;
    }
}