package com.example.projectcafe;

import com.example.projectcafe.classes.Sales;
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
import java.util.List;

public class ReportSale2Controller {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TableView<Sales> salesTableView;

    @FXML
    private TableColumn<Sales, String> nameColumn;

    @FXML
    private TableColumn<Sales, Integer> quantityColumn;

    @FXML
    private TableColumn<Sales, Double> priceColumn;

    @FXML
    private Button backButton;

    private Stage stage;
    private String currentRole;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    private ObservableList<Sales> salesData = FXCollections.observableArrayList();

    public void initialize(List<Sales> topSales) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        salesData.addAll(topSales);
        salesTableView.setItems(salesData);
        backButton.setOnAction(event -> handleBack());
    }
    @FXML
    protected void handleBack() {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report1.fxml"));
                Parent root = loader.load();

                ReportSale1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report1.fxml"));
                Parent root = loader.load();

                ReportSale1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}