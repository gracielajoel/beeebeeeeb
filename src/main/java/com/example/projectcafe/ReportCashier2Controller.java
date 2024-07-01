package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
import com.example.projectcafe.classes.CashierSales;
import com.example.projectcafe.classes.Periode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReportCashier2Controller {

    private Stage stage;
    private String currentRole;
    private String period;

    @FXML
    private Button backButton;

    @FXML
    private TableView<CashierSales> cashierTable;

    @FXML
    private TableColumn<CashierSales,String> nameColumn;

    @FXML
    private TableColumn<CashierSales, Integer> countColumn;

    private ObservableList<CashierSales> cashierSalesList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
        System.out.println("Period set in ReportCashier2Controller: " + period); // Debugging statement
        // Call loadCashierSales only if period is not null or empty
        if (period != null && !period.isEmpty()) {
            loadCashierSales();
        }
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countSales"));
        cashierTable.setItems(cashierSalesList);
        System.out.println("Initializing with period: " + period); // Debugging statement
    }

    private void loadCashierSales() {
        cashierSalesList.clear();
        if (period == null || period.isEmpty()) {
            System.out.println("Period is null or empty in loadCashierSales");
            return; // Avoid querying if period is not set
        }
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT C.cashier_name, COUNT(I.*) AS count_sales FROM invoices I JOIN cashiers C ON ( I.cashier_id = C.cashier_id ) WHERE TO_CHAR(I.date_time, 'FMMonth') || ' ' || EXTRACT(YEAR FROM I.date_time) = ? GROUP BY I.cashier_id, C.cashier_name";

            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, period);

            ResultSet rs = pstmt.executeQuery();  // Use executeQuery for SELECT statement

            while (rs.next()) {
                cashierSalesList.add(new CashierSales(rs.getString("cashier_name"),
                        rs.getInt("count_sales")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cashier-report1.fxml"));
            Parent root = loader.load();

            ReportCashier1Controller controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
