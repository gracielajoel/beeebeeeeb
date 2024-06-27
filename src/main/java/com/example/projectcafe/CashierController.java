package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashierController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

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

    private String name;
    @FXML
    protected void handleLoginButtonAction(ActionEvent actionEvent) throws SQLException {
        name = nameField.getText();
        String password = passwordField.getText();

        if (validateLogin(name, password)) {
            loadCashierTaskPage();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid cashier id or cashier name.");
            alert.showAndWait(); // klo ga diclose, program ga lanjut
        }
    }

    private boolean validateLogin(String name, String password) throws SQLException {
        // ga perlu try catch krn udh ada sqlexception
        Connection db = DatabaseConnection.getConnection();
        String query = "SELECT * FROM cashiers WHERE cashier_name = ?";

        // klo statement, kita tdk bisa memasukkan parameter ke value
        // klo prepared statement, bisa

        try (PreparedStatement pstmt = db.prepareStatement(query)) {
            // index 1 = ? yg pertama
            pstmt.setString(1, name);

            ResultSet result = pstmt.executeQuery();
            Cashier cashier1 = new Cashier();
            return (result.next() && password.equals(cashier1.getPassword()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void loadCashierTaskPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-cashier.fxml"));
            Parent root = loader.load();

            CashierTaskController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(name);

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
