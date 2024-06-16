package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class OwnerTaskController {
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
    protected void handleViewReportButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report-owner.fxml"));
            Parent root = loader.load();

            OwnerReportController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleManagingButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-owner.fxml"));
            Parent root = loader.load();

            OwnerManageController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleExitButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("role.fxml"));
            Parent root = loader.load();

            RoleController controller = loader.getController();
            controller.setStage(stage);

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
