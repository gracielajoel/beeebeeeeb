package com.example.projectcafe;

import com.example.projectcafe.classes.Promo;
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

public class ManagePagePromoController {

    @FXML
    private TableView<Promo> promoTable;

    @FXML
    private TableColumn<Promo, String> periodColumn;

    @FXML
    private TableColumn<Promo, String> paymentColumn;

    @FXML
    private TableColumn<Promo, Double> discountColumn;

    @FXML
    private TableColumn<Promo, String> categoryColumn;

    @FXML
    private TableColumn<Promo, String> menuColumn;

    @FXML
    private TableColumn<Promo, String> promoNameColumn;

    @FXML
    private TextField promoPeriodField;

    @FXML
    private TextField paymentField;

    @FXML
    private TextField discountField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField menuField;

    @FXML
    private TextField promoNameField;

    private Stage stage;

    private ObservableList<Promo> promoList = FXCollections.observableArrayList();

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
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("periode"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menu"));
        promoNameColumn.setCellValueFactory(new PropertyValueFactory<>("promoName"));
        promoTable.setItems(promoList);
        loadPromoData();
    }
    private void loadPromoData() {
        promoList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM promos";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                promoList.add(new Promo(rs.getString("periode_promo"), rs.getString("payment_type"), rs.getDouble("total_discount"), rs.getObject("category_id", Integer.class), rs.getObject("menu_id", Integer.class), rs.getString("promo_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String promoPeriod = promoPeriodField.getText();
        String payment = paymentField.getText();
        String discount = discountField.getText();
        String category = categoryField.getText();
        String menu = menuField.getText();
        String name = promoNameField.getText();

        if (promoPeriod.isEmpty() || payment.isEmpty() || discount.isEmpty() || name.isEmpty() || (category.isEmpty() && menu.isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all required fields (promo period, payment type, discount, and either category or menu)");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query;
            PreparedStatement pstmt;

            if (!category.isEmpty() && menu.isEmpty()) {
                // Jika hanya category yang diisi
                query = "INSERT INTO promos (periode_promo, payment_type, total_discount, category_id, promo_name) " +
                        "VALUES (CAST(? AS DATE), ?, ?, (SELECT category_id FROM categories WHERE product_category = ?), ?)";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1, promoPeriod);
                pstmt.setString(2, payment);
                pstmt.setDouble(3, Double.parseDouble(discount));
                pstmt.setString(4, category);
                pstmt.setString(5,name);
            } else if (category.isEmpty() && !menu.isEmpty()) {
                // Jika hanya menu yang diisi
                query = "INSERT INTO promos (periode_promo, payment_type, total_discount, menu_id, promo_name) " +
                        "VALUES (CAST(? AS DATE), ?, ?, (SELECT menu_id FROM menus WHERE menu_name = ?), ?)";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1, promoPeriod);
                pstmt.setString(2, payment);
                pstmt.setDouble(3, Double.parseDouble(discount));
                pstmt.setString(4, menu);
                pstmt.setString(5,name);
            } else {
                // Jika keduanya diisi (seharusnya tidak terjadi berdasarkan validasi di atas)
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill either category or menu, not both");
                return;
            }

            pstmt.executeUpdate();

            Integer categoryId = -1;
            Integer menuId = -1;

            if (!category.isEmpty()) {
                categoryId = fetchCategoryId(db, category);
            }

            if (!menu.isEmpty()) {
                menuId = fetchMenuId(db, menu);
            }

            promoList.add(new Promo(promoPeriod, payment, Double.parseDouble(discount), categoryId == -1 ? null : categoryId, menuId == -1 ? null : menuId, name));

            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // utk ambil category_id
    private int fetchCategoryId(Connection db, String categoryName) throws SQLException {
        String query = "SELECT category_id FROM categories WHERE product_category = ?";
        PreparedStatement pstmt = db.prepareStatement(query);
        pstmt.setString(1, categoryName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("category_id");
        }
        return -1;
    }

    // utk ambil category_id
    private int fetchMenuId(Connection db, String menuName) throws SQLException {
        String query = "SELECT category_id FROM menus WHERE menu_name = ?";
        PreparedStatement pstmt = db.prepareStatement(query);
        pstmt.setString(1, menuName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("menu_id");
        }
        return -1;
    }

    @FXML
    protected void handleUpdateButton(ActionEvent actionEvent) {
        Promo selectedPromo = promoTable.getSelectionModel().getSelectedItem();
        if (selectedPromo == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a promo to update");
            return;
        }

        String newPromoPeriod = promoPeriodField.getText();
        String newPayment = paymentField.getText();
        String newDiscount = discountField.getText();
        String newCategory = categoryField.getText();
        String newMenu = menuField.getText();
        String newName = promoNameField.getText();

        if (newPromoPeriod.isEmpty() || newPayment.isEmpty() || newDiscount.isEmpty() || (newCategory.isEmpty() && newMenu.isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all required fields (promo period, payment type, discount, and either category or menu)");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query;
            PreparedStatement pstmt;

            if (!newCategory.isEmpty() && newMenu.isEmpty()) {
                // Jika hanya category yang diisi
                query = "UPDATE promos SET periode_promo = CAST(? AS DATE), payment_type = ?, total_discount = ?, category_id = (SELECT category_id FROM categories WHERE product_category = ?), menu_id = NULL, promo_name = ? WHERE promo_name = ? ";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1, newPromoPeriod);
                pstmt.setString(2, newPayment);
                pstmt.setDouble(3, Double.parseDouble(newDiscount));
                pstmt.setString(4, newCategory);
                pstmt.setString(5,newName);

                pstmt.setString(6, selectedPromo.getPromoName());

            } else if (newCategory.isEmpty() && !newMenu.isEmpty()) {
                // Jika hanya menu yang diisi
                query = "UPDATE promos SET periode_promo = CAST(? AS DATE), payment_type = ?, total_discount = ?, category_id = NULL, menu_id = (SELECT menu_id FROM menus WHERE menu_name = ?), promo_name = ? WHERE promo_name = ? ";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1, newPromoPeriod);
                pstmt.setString(2, newPayment);
                pstmt.setDouble(3, Double.parseDouble(newDiscount));
                pstmt.setString(4, newMenu);
                pstmt.setString(5,newName);

                pstmt.setString(6, selectedPromo.getPromoName());
            } else {
                // Jika keduanya diisi (seharusnya tidak terjadi berdasarkan validasi di atas)
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill either category or menu, not both");
                return;
            }

            pstmt.executeUpdate();

            Integer categoryId = -1;
            Integer menuId = -1;

            if (!newCategory.isEmpty()) {
                categoryId = fetchCategoryId(db, newCategory);
            }

            if (!newMenu.isEmpty()) {
                menuId = fetchMenuId(db, newMenu);
            }

            selectedPromo.setPeriode(newPromoPeriod);
            selectedPromo.setPaymentType(newPayment);
            selectedPromo.setTotalDiscount(Double.parseDouble(newDiscount));
            selectedPromo.setCategory(categoryId == -1 ? null : categoryId);
            selectedPromo.setMenu(menuId == -1 ? null : menuId);
            selectedPromo.setPromoName(newName);
            loadPromoData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void handleDeleteButton(ActionEvent actionEvent) {
        Promo selectedPromo = promoTable.getSelectionModel().getSelectedItem();
        if (selectedPromo == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a menu to delete");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM promos WHERE promo_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, selectedPromo.getPromoName());
            pstmt.executeUpdate();

//            userList.remove(selectedUser);
            loadPromoData(); // clear user list, kita ambil lg. maka tdk perlu remove selectedUser
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        promoPeriodField.clear();
        paymentField.clear();
        discountField.clear();
        categoryField.clear();
        menuField.clear();
        promoNameField.clear();
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

    @FXML
    protected void handleMenuAction(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-product.fxml"));
                Parent root = loader.load();

                ManagePageProductController controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");

                // ini utk ganti scene
                // root utk ngisi isi scene
                stage.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-product.fxml"));
                Parent root = loader.load();

                ManagePageProductController controller = loader.getController();
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
}
