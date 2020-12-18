package controllers;

import ScheduleManager.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.LanguageHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private double xOffset, yOffset;

    @FXML
    private AnchorPane anchorContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button contactButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane subAnchorContainer;

    @FXML
    void appointmentButtonOnClick(ActionEvent event) {
        loadAppointmentView();
    }

    @FXML
    void contactButtonOnClick(ActionEvent event) {

    }

    @FXML
    void customerButtonOnClick(ActionEvent event) {

    }

    @FXML
    void exitButtonOnClick(ActionEvent event) {
        Main.getpStage().close();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutButtonOnClick(ActionEvent event) {
        loadLoginView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadAppointmentView();

        appointmentButton.setText(LanguageHandler.getLocaleString("Appointments"));
        customerButton.setText(LanguageHandler.getLocaleString("main_Customers"));
        contactButton.setText(LanguageHandler.getLocaleString("Contacts"));
        exitButton.setText(LanguageHandler.getLocaleString("exit"));
        logoutButton.setText(LanguageHandler.getLocaleString("Logout"));

        // Mouse events to move the window.
        anchorContainer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });
        anchorContainer.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.getpStage().setX(mouseEvent.getScreenX() - xOffset);
                Main.getpStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        System.out.println("MainView.fxml loaded and initialized.");
    }

    private void loadLoginView(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("../views/LoginView.fxml"));
            Scene loginScene = new Scene(root);
            Stage stage = Main.getpStage();
            stage.hide();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("MainViewController loadLoginView() error.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void loadAppointmentView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AppointmentView.fxml"));
            Node appointmentScene = loader.load();
            subAnchorContainer.getChildren().setAll(appointmentScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}