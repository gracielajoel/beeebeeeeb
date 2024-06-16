package com.example.projectcafe;

import com.example.projectcafe.classes.Owner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class OwnerController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private String currentRole;
    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            loadOwnerTaskPage();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait(); // klo ga diclose, program ga lanjut
        }
    }
    private boolean validateLogin(String username, String password) {
        Owner owner = new Owner();
        return username.equals(owner.getUsername()) && password.equals(owner.getPassword());
    }

    private void loadOwnerTaskPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-owner.fxml"));
            Parent root = loader.load();

            OwnerTaskController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("role.fxml"));
            Parent root = loader.load();

            RoleController controller = loader.getController();
            controller.setStage(stage);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
