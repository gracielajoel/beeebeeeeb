package com.example.projectcafe;

import com.example.projectcafe.classes.Menu;
import com.example.projectcafe.classes.Periode;
import com.example.projectcafe.classes.ProductSold;
import com.example.projectcafe.classes.Promo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormatSymbols;
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
    private TableView<Periode> periodTable;

    @FXML
    private TableColumn<Periode,String> monthColumn;

    @FXML
    private TableColumn<Periode, String> yearColumn;

    private ObservableList<Periode> periodeList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        periodTable.setItems(periodeList);
        loadPeriod();
    }
    private void loadPeriod() {
        periodeList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT TO_CHAR(date_time, 'FMMonth') AS month_name, EXTRACT(YEAR FROM date_time) AS year\n FROM invoices;";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                periodeList.add(new Periode(rs.getString("month_name"),
                        rs.getString("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleSubmit(ActionEvent event) {
        String month = monthField.getText();
        String year = yearField.getText();

        try {
            if (isValidDate(month, year)) {
                List<ProductSold> topProduct = fetchTopProducts(month, year);
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
        return (getMonthNumber(month) != -1 && isValidYear(year));
    }

    private boolean isValidYear(String year) {
        try {
            int y = Integer.parseInt(year);
            return y > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getMonthNumber(String monthName) {
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i + 1; // Month number is 1-based
            }
        }
        return -1; // Invalid month
    }

    private List<ProductSold> fetchTopProducts(String month, String year) {
        List<ProductSold> menus = new ArrayList<>();
        int monthNumber = getMonthNumber(month);
        if (monthNumber == -1) {
            return menus;
        }

        String query = """
                    SELECT m.menu_name, c.product_category, SUM(d.qty) as total_quantity
                                    FROM invoices i
                                    JOIN detail_orders d ON i.invoice_id = d.invoice_id
                                    JOIN menus m ON d.menu_id = m.menu_id
                                    JOIN categories c ON m.category_id = c.category_id
                                    WHERE EXTRACT(MONTH FROM i.date_time) = ?
                                    AND EXTRACT(YEAR FROM i.date_time) = ?
                                    GROUP BY m.menu_name, c.product_category
                                    ORDER BY total_quantity DESC;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, monthNumber);
            stmt.setInt(2, Integer.parseInt(year));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String categoryName = rs.getString("product_category");
                String menuName = rs.getString("menu_name");
                int totalSales = rs.getInt("total_quantity");
                menus.add(new ProductSold(categoryName,menuName, totalSales));
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