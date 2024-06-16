package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class RoleController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleOwnerButtonAction(ActionEvent actionEvent) {
        loadOwnerLoginPage();
    }
    private void loadOwnerLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("owner-login.fxml"));
            Parent root = loader.load();

            OwnerController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleCashierButtonAction(ActionEvent actionEvent) {
        loadCashierLoginPage();
    }

    private void loadCashierLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cashier-login.fxml"));
            Parent root = loader.load();

            CashierController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}