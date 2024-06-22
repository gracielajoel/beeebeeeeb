package com.example.projectcafe;

import com.example.projectcafe.classes.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ManagePageAddNewOrder {

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> menuColumn;

    @FXML
    private TableColumn<Order, String> iceColumn;

    @FXML
    private TableColumn<Order, String> sugarColumn;

    @FXML
    private TableColumn<Order, Integer> quantityColumn;

    @FXML
    private TableColumn<Order, String> sizeColumn;

    @FXML
    private TableColumn<Order, Double> priceColumn;

    @FXML
    private TableColumn<Order, String> promoColumn;

    @FXML
    private TableColumn<Order, Double> afterPromoPriceColumn;

    @FXML
    private Label totalPrice;

    @FXML
    private TextField paymentTypeField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField menuField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextField sugarField;

    @FXML
    private TextField iceField;

    @FXML
    private ComboBox<String> promoComboBox;

    private Stage stage;

    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    private LocalDate orderDate;
    private String paymentType;

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
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        iceColumn.setCellValueFactory(new PropertyValueFactory<>("iceLevel"));
        sugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugarLevel"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeChosen"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        promoColumn.setCellValueFactory(new PropertyValueFactory<>("promoUsed"));
        afterPromoPriceColumn.setCellValueFactory(new PropertyValueFactory<>("afterPromoPrice"));
        orderTable.setItems(orderList);
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String menuFieldText = menuField.getText();
        String iceFieldText = iceField.getText();
        String sugarFieldText = sugarField.getText();
        String quantityFieldText = quantityField.getText();
        String sizeFieldText = sizeField.getText();

        if (menuFieldText.isEmpty() || quantityFieldText.isEmpty() || sizeFieldText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Mohon isi semua bidang yang diperlukan dengan benar");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityFieldText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Kuantitas harus berupa angka.");
            return;
        }

        double price = getPriceFromDatabase(menuFieldText, sizeFieldText);
        if (price == 0.0) {
            return;
        }

        double afterPromoPrice = calculateAfterPromoPrice(price, promoComboBox.getValue());

        Order newOrder = new Order(menuFieldText, iceFieldText, sugarFieldText, quantity, sizeFieldText, price, promoComboBox.getValue(), afterPromoPrice);
        orderList.add(newOrder);
        updateTotalPrice();
        clearFields();
    }

    @FXML
    protected void handleDeleteButton(ActionEvent actionEvent) {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderList.remove(selectedOrder);
            updateTotalPrice();
        } else {
            showAlert(Alert.AlertType.ERROR, "Delete Error", "Tidak ada pesanan yang dipilih");
        }
    }

    @FXML
    protected void handleUpdateButton(ActionEvent actionEvent) {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            selectedOrder.setMenuName(menuField.getText());
            selectedOrder.setIceLevel(iceField.getText());
            selectedOrder.setSugarLevel(sugarField.getText());
            selectedOrder.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedOrder.setSizeChosen(sizeField.getText());
            selectedOrder.setPrice(getPriceFromDatabase(menuField.getText(), sizeField.getText()));
            selectedOrder.setPromoUsed(promoComboBox.getValue());
            selectedOrder.setAfterPromoPrice(calculateAfterPromoPrice(selectedOrder.getPrice(), promoComboBox.getValue()));
            orderTable.refresh();
            updateTotalPrice();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Tidak ada pesanan yang dipilih");
        }
    }

    @FXML
    protected void promoAvailable(ActionEvent actionEvent) {
        promoComboBox.getItems().clear();
        LocalDate date = dateField.getValue();
        if (date != null) {
            try (Connection db = DatabaseConnection.getConnection()) {
                // Pertama, dapatkan category_id dari menu
                String getCategoryQuery = "SELECT category_id FROM menus WHERE menu_name = ?";
                int categoryId = -1;
                try (PreparedStatement pstmt = db.prepareStatement(getCategoryQuery)) {
                    pstmt.setString(1, menuField.getText());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            categoryId = rs.getInt("category_id");
                        }
                    }
                }

                // Kemudian, dapatkan promo yang tersedia
                String query = "SELECT promo_name FROM promos " +
                        "WHERE (EXTRACT(MONTH FROM periode_promo) = ? AND EXTRACT(YEAR FROM periode_promo) = ?) " +
                        "AND ((menu_id = (SELECT menu_id FROM menus WHERE menu_name = ?) AND menu_id IS NOT NULL) " +
                        "OR (category_id = ? AND category_id IS NOT NULL) AND payment_type = ?)";
                try (PreparedStatement pstmt = db.prepareStatement(query)) {
                    pstmt.setInt(1, date.getMonthValue());
                    pstmt.setInt(2, date.getYear());
                    pstmt.setString(3, menuField.getText());
                    pstmt.setInt(4, categoryId);
                    pstmt.setString(5, paymentTypeField.getText());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            promoComboBox.getItems().add(rs.getString("promo_name"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error retrieving promo data");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Mohon pilih tanggal terlebih dahulu");
        }
    }

    private double getPriceFromDatabase(String menuName, String size) {
        double price = 0.0;
        String column;

        switch (size.toLowerCase()) {
            case "small":
                column = "small_price";
                break;
            case "medium":
                column = "medium_price";
                break;
            case "large":
                column = "large_price";
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Size Error", "Ukuran tidak valid. Mohon masukkan 'small', 'medium', atau 'large'.");
                return 0.0;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT " + column + " FROM menus WHERE menu_name = ?";
            try (PreparedStatement pstmt = db.prepareStatement(query)) {
                pstmt.setString(1, menuName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        price = rs.getDouble(1);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Menu Error", "Menu tidak ditemukan dalam database.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error retrieving price data.");
        }
        return price;
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

    private void clearFields() {
        menuField.clear();
        iceField.clear();
        sugarField.clear();
        quantityField.clear();
        sizeField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double calculateAfterPromoPrice(double price, String promoName) {
        double discount = 0.0;
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT total_discount FROM promos WHERE promo_name = ?";
            try (PreparedStatement pstmt = db.prepareStatement(query)) {
                pstmt.setString(1, promoName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        discount = rs.getDouble("total_discount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error retrieving promo data");
        }
        return price - (price * discount / 100);
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (Order order : orderList) {
            total += order.getAfterPromoPrice() * order.getQuantity();
        }
        totalPrice.setText(String.format("%.2f", total));
    }

    @FXML
    protected void handleOKbutton(ActionEvent actionEvent) {
        orderDate = dateField.getValue();
        paymentType = paymentTypeField.getText();

        if (orderDate != null && paymentType != null && !paymentType.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Date and Payment Type Set", "Tanggal pesanan diatur ke: " + orderDate + "\nTipe pembayaran diatur ke: " + paymentType);
        } else {
            showAlert(Alert.AlertType.ERROR, "Date or Payment Type Error", "Mohon pilih tanggal dan tipe pembayaran yang valid.");
        }
    }
}
