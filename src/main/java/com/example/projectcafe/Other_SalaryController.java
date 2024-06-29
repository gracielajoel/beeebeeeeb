package com.example.projectcafe;

import com.example.projectcafe.classes.Member;
import com.example.projectcafe.classes.SalaryHistory;
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

public class Other_SalaryController {
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
    private TableView<SalaryHistory> salaryHistoryTable;

    @FXML
    private TableColumn<SalaryHistory,String> nameColumn;

    @FXML
    private TableColumn<SalaryHistory, Double> lastSalaryColumn;

    @FXML
    private TableColumn<SalaryHistory, Double> lastDateColumn;


    private ObservableList<SalaryHistory> salaryHistoryList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        lastSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("lastSalary"));
        lastDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastDate"));
        salaryHistoryTable.setItems(salaryHistoryList);
        loadSalaryHistory();
    }
    private void loadSalaryHistory() {
        salaryHistoryList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT C.cashier_name, S.last_salary, S.last_date FROM salary_histories S JOIN cashiers C ON ( S.cashier_id = C.cashier_id )";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                salaryHistoryList.add(new SalaryHistory(rs.getString("cashier_name"),
                        rs.getDouble("last_salary"),rs.getString("last_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("naikgaji.fxml"));
            Parent root = loader.load();

            Other_NaikGajiController controller = loader.getController();
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
