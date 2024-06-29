package com.example.projectcafe;

import com.example.projectcafe.classes.CashierSalary;
import com.example.projectcafe.classes.PromoUsed;
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

public class Other_NaikGajiController {
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
    private TableView<CashierSalary> cashierReportTable;

    @FXML
    private TableColumn<CashierSalary,String> cashierNameColumn;

    @FXML
    private TableColumn<CashierSalary, Integer> countColumn;

    private ObservableList<CashierSalary> cashierSalaryList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        cashierNameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countSalaryHistory"));
        cashierReportTable.setItems(cashierSalaryList);
        loadCashierSalary();
    }
    private void loadCashierSalary() {
        cashierSalaryList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT C.cashier_name, COUNT(S.*) AS \"count_history\" FROM salary_histories S JOIN cashiers C ON ( S.cashier_id = C.cashier_id ) GROUP BY S.cashier_id, C.cashier_name";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                cashierSalaryList.add(new CashierSalary(rs.getString("cashier_name"),
                        rs.getInt("count_history")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
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

    @FXML
    protected void handleSalaryHistoryButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("salaryhistory.fxml"));
            Parent root = loader.load();

            Other_SalaryController controller = loader.getController();
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
