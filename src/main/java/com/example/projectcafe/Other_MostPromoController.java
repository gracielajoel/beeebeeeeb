package com.example.projectcafe;

import com.example.projectcafe.classes.CashierSales;
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

public class Other_MostPromoController {
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
    private TableView<PromoUsed> promoUsedTable;

    @FXML
    private TableColumn<PromoUsed,String> promoNameColumn;

    @FXML
    private TableColumn<PromoUsed, Integer> countColumn;

    private ObservableList<PromoUsed> promoUsedList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        promoNameColumn.setCellValueFactory(new PropertyValueFactory<>("promoName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("promoUsed"));
        promoUsedTable.setItems(promoUsedList);
        loadPromoCounts();
    }
    private void loadPromoCounts() {
        promoUsedList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT P.promo_name, COUNT(D.*) AS \"promo_used\" FROM detail_orders D JOIN promos P ON ( D.promo_id = P.promo_id ) GROUP BY D.promo_id, P.promo_name";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                promoUsedList.add(new PromoUsed(rs.getString("promo_name"),
                        rs.getInt("promo_used")));
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
}
