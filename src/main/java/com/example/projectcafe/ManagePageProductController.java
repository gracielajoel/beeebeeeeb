package com.example.projectcafe;

import com.example.projectcafe.classes.Member;
import com.example.projectcafe.classes.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagePageProductController {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TableView<Menu> menuTable;

    @FXML
    private TableColumn<Menu,String> nameColumn;

    @FXML
    private TableColumn<Menu, Double> smallPriceColumn;

    @FXML
    private TableColumn<Menu, Double> mediumPriceColumn;

    @FXML
    private TableColumn<Menu, Double> largePriceColumn;

    @FXML
    private TableColumn<Menu, String> categoryColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField smallPriceField;

    @FXML
    private TextField mediumPriceField;

    @FXML
    private TextField largePriceField;

    @FXML
    private TextField categoryField;

    private Stage stage;

    private ObservableList<Menu> menuList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private String currentRole;
    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        smallPriceColumn.setCellValueFactory(new PropertyValueFactory<>("smallPrice"));
        mediumPriceColumn.setCellValueFactory(new PropertyValueFactory<>("mediumPrice"));
        largePriceColumn.setCellValueFactory(new PropertyValueFactory<>("largePrice"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        menuTable.setItems(menuList);
        loadMenuData();
    }
    private void loadMenuData() {
        menuList.clear();
        String query = "SELECT M.menu_name, M.small_price, M.medium_price, M.large_price, C.product_category " +
                "FROM menus M JOIN categories C ON M.category_id = C.category_id";
        try (Connection db = DatabaseConnection.getConnection();
             PreparedStatement stmt = db.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                menuList.add(new Menu(
                        rs.getString("menu_name"),
                        rs.getDouble("small_price"),
                        rs.getDouble("medium_price"),
                        rs.getDouble("large_price"),
                        rs.getString("product_category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String menuName = nameField.getText();
        String smallPrice = smallPriceField.getText();
        String mediumPrice = mediumPriceField.getText();
        String largePrice = largePriceField.getText();
        String category = categoryField.getText();

        if (menuName.isEmpty() || smallPrice.isEmpty() || mediumPrice.isEmpty() || largePrice.isEmpty() || category.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields correctly");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO menus (menu_name, small_price, medium_price, large_price, category_id) " +
                    "VALUES (?, ?, ?, ?, (SELECT category_id FROM categories WHERE product_category = ?))";

            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, menuName);
            pstmt.setDouble(2, Double.parseDouble(smallPrice));
            pstmt.setDouble(3, Double.parseDouble(mediumPrice));
            pstmt.setDouble(4, Double.parseDouble(largePrice));
            pstmt.setString(5, category);

            pstmt.executeUpdate();

            menuList.add(new Menu(menuName, Double.parseDouble(smallPrice), Double.parseDouble(mediumPrice), Double.parseDouble(largePrice), category));
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void handleUpdateButton(ActionEvent actionEvent) {
        Menu selectedMenu = menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenu == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a menu to update");
            return;
        }

        String newMenuName = nameField.getText();
        String newSmallPrice = smallPriceField.getText();
        String newMediumPrice = mediumPriceField.getText();
        String newLargePrice = largePriceField.getText();
        String newCategory = categoryField.getText();

        if (newMenuName.isEmpty() || newSmallPrice.isEmpty() || newMediumPrice.isEmpty() || newLargePrice.isEmpty() || newCategory.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields correctly");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "UPDATE menus SET menu_name = ?, small_price = ?, medium_price = ?, large_price = ?, " +
                    "category_id = (SELECT category_id FROM categories WHERE product_category = ?) WHERE menu_name = ?";

            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, newMenuName);
            pstmt.setDouble(2, Double.parseDouble(newSmallPrice));
            pstmt.setDouble(3, Double.parseDouble(newMediumPrice));
            pstmt.setDouble(4, Double.parseDouble(newLargePrice));
            pstmt.setString(5, newCategory);
            pstmt.setString(6, selectedMenu.getMenuName());
            pstmt.executeUpdate();

            selectedMenu.setMenuName(newMenuName);
            selectedMenu.setSmallPrice(Double.parseDouble(newSmallPrice));
            selectedMenu.setMediumPrice(Double.valueOf(newMediumPrice));
            selectedMenu.setLargePrice(Double.valueOf(newLargePrice));
            selectedMenu.setCategory(newCategory);
            loadMenuData();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleDeleteButton(ActionEvent actionEvent) {
        Menu selectedMenu = menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenu == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a menu to delete");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM menus WHERE menu_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, selectedMenu.getMenuName());
            pstmt.executeUpdate();

//            userList.remove(selectedUser);
            loadMenuData(); // clear user list, kita ambil lg. maka tdk perlu remove selectedUser
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        smallPriceField.clear();
        mediumPriceField.clear();
        largePriceField.clear();
        categoryField.clear();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-cashier.fxml"));
                Parent root = loader.load();

                CashierManageController controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-owner.fxml"));
                Parent root = loader.load();

                OwnerManageController controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
