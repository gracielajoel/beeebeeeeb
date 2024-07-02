package com.example.projectcafe;

import com.example.projectcafe.classes.Order;
import com.example.projectcafe.classes.Promo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ManagePageAddNewOrder {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }


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
    private TableColumn<Order, Double> tempTotalColumn;

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
    private TextField customerNameField;

    @FXML
    private Label cashierName;

    @FXML
    private TextField memberIDField;

    @FXML
    private TextField payField;

    @FXML
    private Label changeLabel;

    @FXML
    private Label pointLabel;

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
    private ComboBox<Promo> promoComboBox;

    @FXML
    private TextField timeField;

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
        tempTotalColumn.setCellValueFactory(new PropertyValueFactory<>("tempTotal"));
        promoColumn.setCellValueFactory(new PropertyValueFactory<>("promoUsed"));
        afterPromoPriceColumn.setCellValueFactory(new PropertyValueFactory<>("afterPromoPrice"));
        orderTable.setItems(orderList);

        promoComboBox.setConverter(new StringConverter<Promo>() {
            @Override
            public String toString(Promo promo) {
                return promo != null ? promo.getPromoName() : "";
            }

            @Override
            public Promo fromString(String string) {
                return promoComboBox.getItems().stream().filter(promo -> promo.getPromoName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String timeFieldText = timeField.getText();
        String menuFieldText = menuField.getText();
        String iceFieldText = iceField.getText();
        String sugarFieldText = sugarField.getText();
        String quantityFieldText = quantityField.getText();
        String sizeFieldText = sizeField.getText();

        if (timeFieldText.isEmpty() || menuFieldText.isEmpty() || quantityFieldText.isEmpty() || sizeFieldText.isEmpty()) {
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

        Promo promoChosen = promoComboBox.getValue();

        double afterPromoPrice = calculateAfterPromoPrice(price, promoChosen, quantity);
        double tempTotal = price * quantity;

        Order newOrder = new Order(menuFieldText, iceFieldText, sugarFieldText, quantity, sizeFieldText, price, promoChosen != null ? promoChosen.getPromoName() : null, afterPromoPrice, tempTotal);
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
            String menuFieldText = menuField.getText();
            String iceFieldText = iceField.getText();
            String sugarFieldText = sugarField.getText();
            String quantityFieldText = quantityField.getText();
            String sizeFieldText = sizeField.getText();

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

            Promo promochosen = promoComboBox.getValue();
            double afterPromoPrice = calculateAfterPromoPrice(price, promochosen, quantity);
            double tempTotal = price * quantity;

            selectedOrder.setMenuName(menuFieldText);
            selectedOrder.setIceLevel(iceFieldText);
            selectedOrder.setSugarLevel(sugarFieldText);
            selectedOrder.setQuantity(quantity);
            selectedOrder.setSizeChosen(sizeFieldText);
            selectedOrder.setPrice(price);
            selectedOrder.setPromoUsed(promochosen != null ? promochosen.getPromoName() : null);
            selectedOrder.setAfterPromoPrice(afterPromoPrice);
            selectedOrder.setTempTotal(tempTotal); // Update tempTotal
            orderTable.refresh();
            updateTotalPrice();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Tidak ada pesanan yang dipilih");
        }
    }

    @FXML
    protected void promoAvailable(ActionEvent actionEvent) {
        LocalDate date = dateField.getValue();
        String menuName = menuField.getText();
        String paymentType = paymentTypeField.getText();

        if (date != null && !menuName.isEmpty() && !paymentType.isEmpty()) {
            ObservableList<Promo> promos = loadPromos(date, menuName, paymentType);
            promoComboBox.getItems().clear(); // Clear combo box before adding new items
            promoComboBox.setItems(promos);

            // Handle selection change event
            promoComboBox.setOnAction(event -> {
                Promo selectedPromo = promoComboBox.getSelectionModel().getSelectedItem();
                if (selectedPromo != null) {
                    System.out.println("Promo yang dipilih: " + selectedPromo.getPromoName());
                    orderTable.refresh(); // Refresh table view to reflect changes
                    updateTotalPrice(); // Recalculate total price
                } else {
                    orderTable.refresh(); // Refresh table view to reflect changes
                    updateTotalPrice(); // Recalculate total price
                }
            });

            // Select first promo as default if available
            if (!promos.isEmpty()) {
                promoComboBox.getSelectionModel().selectFirst();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Mohon isi semua bidang yang diperlukan dengan benar");
        }
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

    private void updateTotalPrice() {
        double total = 0.0;
        for (Order order : orderList) {
            total += order.getAfterPromoPrice();
        }
        totalPrice.setText(String.valueOf(total));
    }


    private ObservableList<Promo> loadPromos(LocalDate date, String menuName, String paymentType) {
        ObservableList<Promo> promoList = FXCollections.observableArrayList();
        try (Connection db = DatabaseConnection.getConnection()) {
            String getCategoryQuery = "SELECT category_id FROM menus WHERE menu_name = ?";
            int categoryId = -1;
            try (PreparedStatement pstmt = db.prepareStatement(getCategoryQuery)) {
                pstmt.setString(1, menuName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        categoryId = rs.getInt("category_id");
                    }
                }
            }

            String query = "SELECT * FROM promos " +
                    "WHERE (EXTRACT(MONTH FROM periode_promo) = ? AND EXTRACT(YEAR FROM periode_promo) = ?) " +
                    "AND payment_type = ? AND ((menu_id = (SELECT menu_id FROM menus WHERE menu_name = ?) AND menu_id IS NOT NULL) " +
                    "OR (category_id = ? AND category_id IS NOT NULL) )";
            try (PreparedStatement pstmt = db.prepareStatement(query)) {
                pstmt.setInt(1, date.getMonthValue());
                pstmt.setInt(2, date.getYear());
                pstmt.setString(3, paymentType);
                pstmt.setString(4, menuName);
                pstmt.setInt(5, categoryId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Promo promo = new Promo(
                                rs.getString("periode_promo"),
                                rs.getString("payment_type"),
                                rs.getDouble("total_discount"),
                                rs.getString("category_id"),
                                rs.getString("menu_id"),
                                rs.getString("promo_name")
                        );
                        promoList.add(promo);
                        System.out.println("Loaded promo: " + promo.getPromoName());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error retrieving promo data");
        }
        return promoList;
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

    private double calculateAfterPromoPrice(double price, Promo promo, int quantity) {
        if (promo == null) {
            return price * quantity;
        }
        double discount = promo.getTotalDiscount();
        double discountedPrice = price - (price * discount);
        return discountedPrice * quantity;
    }

    @FXML
    protected void handleOKbutton(ActionEvent actionEvent) {

        cashierName.setText(cashier_name);
        orderDate = dateField.getValue();
        paymentType = paymentTypeField.getText();

        if (orderDate != null && paymentType != null && !paymentType.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Date and Payment Type Set", "Tanggal pesanan diatur ke: " + orderDate + "\nTipe pembayaran diatur ke: " + paymentType);
        } else {
            showAlert(Alert.AlertType.ERROR, "Date or Payment Type Error", "Mohon pilih tanggal dan tipe pembayaran yang valid.");
        }
    }

    @FXML
    protected void handleCheckButton(ActionEvent actionEvent) {
        String menuName = menuField.getText();
        String paymentType = paymentTypeField.getText();
        LocalDate date = dateField.getValue();

        if (menuName.isEmpty() || paymentType.isEmpty() || date == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Mohon isi semua bidang yang diperlukan dengan benar");
            return;
        }

        ObservableList<Promo> promos = loadPromos(date, menuName, paymentType);
        if (!promos.isEmpty()) {
            promoComboBox.getItems().clear();
            promoComboBox.setItems(promos);
        } else {
            promoComboBox.getItems().clear();
            showAlert(Alert.AlertType.INFORMATION, "No Promos", "Tidak ada promo yang tersedia untuk kriteria ini.");
        }
    }

    private int getCashierId(Connection db, String name) throws SQLException {
        String query = "SELECT cashier_id FROM cashiers WHERE cashier_name = ?";
        PreparedStatement pstmt = db.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("cashier_id");
        }
        return -1;
    }

    private int invoiceId;

    @FXML
    protected void handleInvoiceButton(ActionEvent actionEvent) {
        try (Connection db = DatabaseConnection.getConnection()) {

            Timestamp datetime = Timestamp.valueOf(dateField.getValue() + " " + timeField.getText());
            String customerName = customerNameField.getText();
            Double total = Double.valueOf(totalPrice.getText());
            Double pay = Double.valueOf(payField.getText());
            String paymentType = paymentTypeField.getText();
            Double change = Double.valueOf(changeLabel.getText());
            String addPoint = pointLabel.getText();
            Integer cashierId = getCashierId(db, cashierName.getText());
            String memberId = memberIDField.getText();

            if (String.valueOf(datetime).isEmpty() || customerName.isEmpty() || paymentType.isEmpty() || cashierId == -1 ) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Mohon isi semua bidang yang diperlukan dengan benar.");
                return;
            }

            System.out.println(cashierId);
            System.out.println(datetime);

            if ( memberId.isEmpty() ){
                String query = "INSERT INTO invoices (date_time, customer_name, total_price, pay, payment_method, change, cashier_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setTimestamp(1, datetime);
                pstmt.setString(2, customerName);
                pstmt.setDouble(3, total);
                pstmt.setDouble(4, pay);
                pstmt.setString(5, paymentType);
                pstmt.setDouble(6, change);
                pstmt.setInt(7, cashierId);

                pstmt.executeUpdate();

                ResultSet rs2 = pstmt.getGeneratedKeys();
                if (rs2.next()) {
                    invoiceId = rs2.getInt(1);
                }

            } else {
                String query = "INSERT INTO invoices (date_time, customer_name, total_price, pay, payment_method, change, add_point, cashier_id, member_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setTimestamp(1, datetime);
                pstmt.setString(2, customerName);
                pstmt.setDouble(3, total);
                pstmt.setDouble(4, pay);
                pstmt.setString(5, paymentType);
                pstmt.setDouble(6, change);
                pstmt.setInt(7, Integer.parseInt(addPoint));
                pstmt.setInt(8, cashierId);
                pstmt.setInt(9, Integer.parseInt(memberId));

                pstmt.executeUpdate();

                ResultSet rs2 = pstmt.getGeneratedKeys();
                if (rs2.next()) {
                    invoiceId = rs2.getInt(1);
                }

                // update member points
                String addMemberPoint = "UPDATE members SET member_point = ? WHERE member_id = ?";
                PreparedStatement pstmt3 = db.prepareStatement(addMemberPoint);
                pstmt3.setInt(1, Integer.parseInt(addPoint));
                pstmt3.setInt(2, Integer.parseInt(memberId));
                pstmt3.executeUpdate();
            }

            // add to detail orders
            addToDetailOrders(db, invoiceId);

            clearFields();
            timeField.clear();
            paymentTypeField.clear();
            payField.clear();
            customerNameField.clear();
            memberIDField.clear();
            orderList.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int getMenuId(Connection db, String menuName)throws SQLException{
        String query = "SELECT menu_id FROM menus WHERE menu_name = ?";
        PreparedStatement pstmt = db.prepareStatement(query);
        pstmt.setString(1, menuName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("menu_id");
        }
        return -1;
    }

    private int getPromoId(Connection db, String promoName) throws SQLException {
        String query = "SELECT promo_id FROM promos WHERE promo_name = ?";
        PreparedStatement pstmt = db.prepareStatement(query);
        pstmt.setString(1, promoName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("promo_id");
        }
        return -1;
    }
    private void addToDetailOrders(Connection db, int invoice_id)throws SQLException{
        String detailOrdersSql = "INSERT INTO detail_orders (detail_order_id, qty, size_chosen, sugar_level, ice_level, menu_id, promo_id, invoice_id) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement detailOrdersStmt = db.prepareStatement(detailOrdersSql)) {
            for (Order order : orderList) {
                detailOrdersStmt.setString(2, order.getMenuName());
                detailOrdersStmt.setInt(1, order.getQuantity());
                detailOrdersStmt.setString(2, order.getSizeChosen());
                detailOrdersStmt.setString(3, order.getSugarLevel());
                detailOrdersStmt.setString(4, order.getIceLevel());
                detailOrdersStmt.setInt(5,getMenuId(db,order.getMenuName()));
                if ( getPromoId(db,order.getPromoUsed()) == -1 ){
                    detailOrdersStmt.setNull(6,java.sql.Types.INTEGER);
                } else {
                    detailOrdersStmt.setInt(6, getPromoId(db, order.getPromoUsed()));
                }
                detailOrdersStmt.setInt(7, invoice_id);
                detailOrdersStmt.addBatch();
            }

            detailOrdersStmt.executeBatch();
        }
    }

    private static int calculatePoints(Double totalPrice) {
        // Tentukan base points dengan membagi total harga dengan 10000
        int basePoints = (int) (totalPrice / 10000);

        // Tentukan tambahan poin berdasarkan sisa pembagian
        int additionalPoints = 0;
        int remainder = (int) (totalPrice % 10000);

        if (remainder >= 5000) {
            additionalPoints = 1;
        }

        // Mengkombinasikan basePoints dan additionalPoints
        int points = basePoints + additionalPoints;

        return points;
    }
    @FXML
    protected void handleConfirmPayment(ActionEvent actionEvent) {
        Double total = Double.valueOf(totalPrice.getText());

        Integer addPoint = calculatePoints(total);
        if ( !memberIDField.getText().isEmpty()) {
            pointLabel.setText(String.valueOf(addPoint));
        } else {
            pointLabel.setText(null);
        }

        Double payment = Double.valueOf(payField.getText());
        changeLabel.setText(String.valueOf(payment-total));
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

    @FXML
    protected void handleMemberButton(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-member.fxml"));
                Parent root = loader.load();

                ManagePageMemberController controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                // ini utk ganti scene
                // root utk ngisi isi scene
                stage.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("managepage-member.fxml"));
                Parent root = loader.load();

                ManagePageMemberController controller = loader.getController();
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
