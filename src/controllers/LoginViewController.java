package controllers;

import ScheduleManager.Main;
import com.sun.glass.ui.EventLoop;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.User;
import utils.DBQuery;
import utils.LanguageHandler;


public class LoginViewController implements Initializable {

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    private double xOffset, yOffset;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML VARIABLES --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private AnchorPane containerAnchorPane;

    @FXML
    private Button exitButton;

    @FXML
    private HBox usernameBox;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private HBox passwordBox;

    @FXML
    private Button signInButton;

    @FXML
    private Label locationLabel;

    // -----------------------------------------------------------------------------------------------------------------
    // FXML Methods ----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL var1, ResourceBundle var2){

        // Language Conversion
        locationLabel.setText(LanguageHandler.getLocaleString("office_location") + ": " + User.getUserLocation());
        usernameTextField.promptTextProperty().set(LanguageHandler.getLocaleString("username"));
        passwordTextField.promptTextProperty().set(LanguageHandler.getLocaleString("password"));
        signInButton.setText(LanguageHandler.getLocaleString("signin"));
        exitButton.setText(LanguageHandler.getLocaleString("exit"));

        // Mouse events to move the window.
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
                Main.getpStage().setX(mouseEvent.getScreenX() - xOffset);
                Main.getpStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        // Text Field Focus Designs
        usernameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    usernameBox.setStyle(
                            "-fx-background-color: #dfe6e9; " +
                                    "-fx-border-radius: 10;" +
                                    " -fx-background-radius: 10; " +
                                    "-fx-border-style: none none none solid;" +
                                    " -fx-border-width: 1; " +
                                    "-fx-border-color: #0984e3;");
                }
                else
                {
                    usernameBox.setStyle(
                            "-fx-background-color: #dfe6e9; " +
                                    "-fx-border-radius: 10; " +
                                    " -fx-background-radius: 10; ");
                }
            }
        });
        passwordTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    passwordBox.setStyle(
                            "-fx-background-color: #dfe6e9; " +
                                    "-fx-border-radius: 10;" +
                                    " -fx-background-radius: 10; " +
                                    "-fx-border-style: none none none solid;" +
                                    " -fx-border-width: 1; " +
                                    "-fx-border-color: #0984e3;");
                }
                else
                {
                    passwordBox.setStyle(
                            "-fx-background-color: #dfe6e9; " +
                                    "-fx-border-radius: 10; " +
                                    " -fx-background-radius: 10; ");
                }
            }
        });

        // This keeps the username textfield from being focused on startup.
        Platform.runLater(() -> {
                usernameBox.requestFocus();
        });
    }

    /**
     * Closes the program when the exit button is clicked.
     * @param   event   The event that caused the function to be called.
     */
    @FXML
    void exitButtonOnClick(ActionEvent event) {
        Main.getpStage().close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Validates the information entered into the username and password field.
     * @param event event that caused the function to be called.
     */
    @FXML
    void signInButtonOnClick(ActionEvent event) throws IOException {
        if(usernameTextField.getText().isEmpty()){
            loginMessageLabel.setTextFill(Color.web("#d63031"));
            loginMessageLabel.setText(LanguageHandler.getLocaleString("username_empty"));
            return;
        }
        if(passwordTextField.getText().isEmpty()){
            loginMessageLabel.setTextFill(Color.web("#d63031"));
            loginMessageLabel.setText(LanguageHandler.getLocaleString("password_empty"));
            return;
        }

        if(DBQuery.validateLogin(usernameTextField.getText(), passwordTextField.getText())){
            loginMessageLabel.setText("");
            System.out.println("Login Successful!");
            Parent root = FXMLLoader.load(getClass().getResource("../views/MainView.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = Main.getpStage();
            primaryStage.hide();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            loginMessageLabel.setTextFill(Color.web("#d63031"));
            loginMessageLabel.setText(LanguageHandler.getLocaleString("login_invalid"));
            System.out.println("Login Unsuccessful!");
        }
    }
}
