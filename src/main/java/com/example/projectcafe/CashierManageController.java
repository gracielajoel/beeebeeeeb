package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierManageController {

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
    protected void handleManageProductButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-product.fxml"));
            Parent root = loader.load();

            ManagePageProductController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleManagePromoButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-promo.fxml"));
            Parent root = loader.load();

            ManagePagePromoController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleManageMemberButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-member.fxml"));
            Parent root = loader.load();

            ManagePageMemberController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-cashier.fxml"));
            Parent root = loader.load();

            CashierTaskController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddNewOrderButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-order.fxml"));
            Parent root = loader.load();

            ManagePageAddNewOrder controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
