package com.example.projectcafe;

import com.example.projectcafe.classes.CashierSales;
import com.example.projectcafe.classes.ProductSold;
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

public class Other_TopProductController {
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
    private TableView<ProductSold> bestMenuTable;

    @FXML
    private TableColumn<ProductSold,String> categoryColumn;

    @FXML
    private TableColumn<ProductSold,String> menuNameColumn;

    @FXML
    private TableColumn<ProductSold, Integer> countColumn;

    private ObservableList<ProductSold> bestMenuList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("menuSum"));
        bestMenuTable.setItems(bestMenuList);
        loadBestMenu();
    }
    private void loadBestMenu() {
        bestMenuList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT C.product_category, M.menu_name, SUM(D.qty) AS \"product_soldout\" FROM detail_orders D JOIN menus M ON ( D.menu_id = M.menu_id ) JOIN categories C ON ( M.category_id = C.category_id ) GROUP BY C.product_category, D.menu_id, M.menu_name ORDER BY product_soldout DESC LIMIT 5";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                bestMenuList.add(new ProductSold(rs.getString("product_category"),rs.getString("menu_name"),
                        rs.getInt("product_soldout")));
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
