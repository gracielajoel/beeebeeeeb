package com.example.projectcafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.projectcafe.classes.Promo;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReportPromo2Controller {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TableView<Promo> promoTable;

    @FXML
    private TableColumn<Promo, String> promoNameColumn;

    @FXML
    private TableColumn<Promo, Integer> promoCountColumn;

    @FXML
    private Button backButton;

    private Stage stage;
    private String currentRole;

    private ObservableList<Promo> promoData = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
        System.out.println(currentRole);
    }

    public ReportPromo2Controller(){

    }

    public void initialize(List<Promo> topPromo) {
        promoNameColumn.setCellValueFactory(new PropertyValueFactory<>("promoName"));
        promoCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.promoData.addAll(topPromo);
        this.promoTable.setItems(promoData);
        this.backButton.setOnAction((event) -> handleBack());
    }

    @FXML
    protected void handleBack() {
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
}