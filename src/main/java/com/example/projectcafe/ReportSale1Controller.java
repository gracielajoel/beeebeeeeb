package com.example.projectcafe;

import com.example.projectcafe.classes.Periode;
import com.example.projectcafe.classes.Sales;
import com.example.projectcafe.classes.SalesPeriod;
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


    private Stage stage;

    private String currentRole;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @FXML
    private TableView<SalesPeriod> salesPeriodTable;

    @FXML
    private TableColumn<SalesPeriod,String> monthColumn;

    @FXML
    private TableColumn<SalesPeriod, String> yearColumn;

    @FXML
    private TableColumn<SalesPeriod, Double> totalColumn;

    private ObservableList<SalesPeriod> salesPeriodList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        salesPeriodTable.setItems(salesPeriodList);
        loadPeriod();
    }
    private void loadPeriod() {
        salesPeriodList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT TO_CHAR(date_time, 'FMMonth') AS month, EXTRACT(YEAR FROM date_time) AS year, SUM(total_price) AS sum_price FROM invoices GROUP BY TO_CHAR(date_time, 'FMMonth'), EXTRACT(YEAR FROM date_time)";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                salesPeriodList.add(new SalesPeriod(rs.getString("month"),
                        rs.getString("year"), rs.getDouble("sum_price")));
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
                List<Sales> topSales = fetchTopSales(month, year);
                if (!topSales.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report2.fxml"));
                    Parent root = loader.load();

                    ReportSale2Controller controller = loader.getController();
                    controller.initialize(topSales);
                    controller.setStage(stage);
                    controller.setCurrentRole(currentRole);


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

    private List<Sales> fetchTopSales(String month, String year) {
        List<Sales> sales = new ArrayList<>();
        int monthNumber = getMonthNumber(month);
        if (monthNumber == -1) {
            return sales;
        }

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
                    ORDER BY total_quantity DESC;   
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, monthNumber);
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