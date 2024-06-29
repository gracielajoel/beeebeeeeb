package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
import com.example.projectcafe.classes.CashierSales;
import com.example.projectcafe.classes.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Other_CashierSalesController {
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
    private TableView<CashierSales> cashierSalesTable;

    @FXML
    private TableColumn<CashierSales,String> nameColumn;

    @FXML
    private TableColumn<CashierSales, Integer> countColumn;

    private ObservableList<CashierSales> cashierSalesList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countSales"));
        cashierSalesTable.setItems(cashierSalesList);
        loadCashierSales();
    }
    private void loadCashierSales() {
        cashierSalesList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT C.cashier_name, COUNT(I.*) AS \"count_sales\" FROM invoices I JOIN cashiers C ON ( I.cashier_id = C.cashier_id ) GROUP BY I.cashier_id, C.cashier_name ORDER BY count_sales DESC";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                cashierSalesList.add(new CashierSales(rs.getString("cashier_name"),
                        rs.getInt("count_sales")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected
    void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("other-report.fxml"));
            Parent root = loader.load();

            ReportOthersController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
