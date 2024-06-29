package com.example.projectcafe;

import com.example.projectcafe.classes.Sales;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportSale1Controller {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TextField monthField;

    @FXML
    private TextField yearField;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    private Stage stage;

    private String currentRole;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @FXML
    protected void initialize() {
        submitButton.setOnAction(event -> handleSubmit(event));
        backButton.setOnAction(event -> handleBack(event));

    }
    @FXML
    protected void handleSubmit(ActionEvent event) {
        String month = monthField.getText();
        String year = yearField.getText();

        try {
            if (isValidDate(month, year)) {
                List<Sales> topSales = fetchTopSales(month, year);
                if (!topSales.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report2.fxml"));
                    Parent root = loader.load();

                    ReportSale2Controller controller = loader.getController();
                    controller.initialize(topSales);
                    controller.setStage(stage);
                    controller.setCurrentRole(currentRole);

                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } else {
                    showAlert("No sales found for the given month and year.");
                }
            } else {
                showAlert("Invalid month or year. Please enter valid numbers.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Month: " + month + ", Year: " + year);
    }

    @FXML
    protected void handleBack(ActionEvent event) {

        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("report-cashier.fxml"));
                Parent root = loader.load();

                CashierReportController controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
    }

    private boolean isValidDate(String month, String year) {
        try {
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);
            return (m >= 1 && m <= 12 && y > 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<Sales> fetchTopSales(String month, String year) {
        List<Sales> sales = new ArrayList<>();

        String query = """
                    SELECT m.menu_name,SUM(d.qty) AS total_quantity, SUM(d.qty * CASE
                    WHEN d.size_chosen = 'small' THEN m.small_price
                    WHEN d.size_chosen = 'medium' THEN m.medium_price
                    WHEN d.size_chosen = 'large' THEN m.large_price
                    END) AS total_price
                    FROM detail_orders d JOIN menus m ON d.menu_id = m.menu_id
                    JOIN invoices i ON d.invoice_id = i.invoice_id
                    WHERE EXTRACT(MONTH FROM i.date_time) = ?
                    AND EXTRACT(YEAR FROM i.date_time) = ?
                    GROUP BY m.menu_id, m.menu_name, m.small_price, m.medium_price, m.large_price
                    ORDER BY total_quantity DESC
                    LIMIT 5;   
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(month));
            stmt.setInt(2, Integer.parseInt(year));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String menuName = rs.getString("menu_name");
                int totalQuantity = rs.getInt("total_quantity");
                double totalPrice = rs.getDouble("total_price");
                sales.add(new Sales(menuName, totalQuantity, totalPrice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}