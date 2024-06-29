package com.example.projectcafe;

import com.example.projectcafe.classes.Menu;
import com.example.projectcafe.classes.Promo;
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

public class ReportProduct1Controller{

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
                List<Menu> topProduct = fetchTopProducts(month, year);
                if (!topProduct.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("product-report2.fxml"));
                    Parent root = loader.load();

                    ReportProduct2Controller controller = loader.getController();
                    controller.initialize(topProduct);
                    controller.setStage(stage);
                    controller.setCurrentRole(currentRole);

                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } else {
                    showAlert("No product found for the given month and year.");
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
        if (currentRole == null) {
            System.err.println("currentRole is not initialized!");
            return;
        }
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

    private List<Menu> fetchTopProducts(String month, String year) {
        List<Menu> menus = new ArrayList<>();

        String query = """
                    SELECT m.menu_name, m.category_id, SUM(d.qty) as total_quantity
                                    FROM invoices i
                                    JOIN detail_orders d ON i.invoice_id = d.invoice_id
                                    JOIN menus m ON d.menu_id = m.menu_id
                                    WHERE EXTRACT(MONTH FROM i.date_time) = ?
                                    AND EXTRACT(YEAR FROM i.date_time) = ?
                                    GROUP BY m.menu_name, m.category_id
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
                int categoryName = rs.getInt("category_id");
                int totalSales = rs.getInt("total_quantity");
                //menus.add(new Menu(menuName, null, null, null, categoryName, totalSales));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menus;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}