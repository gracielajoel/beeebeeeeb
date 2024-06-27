package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierTaskController {
    private Stage stage;
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }
    private String currentRole;
    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleViewReportButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report-cashier.fxml"));
            Parent root = loader.load();

            CashierReportController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleManagingButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-cashier.fxml"));
            Parent root = loader.load();

            CashierManageController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

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
