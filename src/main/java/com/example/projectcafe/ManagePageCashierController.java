package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
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
import java.sql.*;

public class ManagePageCashierController {

    @FXML
    private TableView<Cashier> cashierTable;

    @FXML
    private TableColumn<Cashier,String> nameColumn;

    @FXML
    private TableColumn<Cashier, Double> salaryColumn;

    @FXML
    private TableColumn<Cashier, Double> commissionColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField commissionField;

    private Stage stage;
    private ObservableList<Cashier> cashierList = FXCollections.observableArrayList();


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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        commissionColumn.setCellValueFactory(new PropertyValueFactory<>("commission"));
        cashierTable.setItems(cashierList);
        loadCashierData();
    }
    private void loadCashierData() {
        cashierList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cashiers";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                cashierList.add(new Cashier(rs.getString("cashier_name"), rs.getDouble("salary"), rs.getDouble("commission")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String cashierName = nameField.getText();
        String salaryText = salaryField.getText().trim(); // Trim untuk menghapus spasi di awal dan akhir
        String commissionText = commissionField.getText().trim(); // Trim untuk menghapus spasi di awal dan akhir

        if (cashierName.isEmpty() || salaryText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter cashier name and salary");
            return;
        }

        Double salary = null;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Invalid salary format");
            return;
        }

        Double commission = null;
        if (!commissionText.isEmpty()) {
            try {
                commission = Double.parseDouble(commissionText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error!", "Invalid commission format");
                return;
            }
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO cashiers (cashier_name, salary, commission) VALUES (?, ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, cashierName);
            pstmt.setDouble(2, salary);

            if (commission == null) {
                pstmt.setNull(3, Types.DOUBLE);
            } else {
                pstmt.setDouble(3, commission);
            }

            pstmt.executeUpdate();
            cashierList.add(new Cashier(cashierName, salary, commission));
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void handleUpdateButton(ActionEvent actionEvent) {
        Cashier selectedUser = cashierTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to update");
            return;
        }

        String newName = nameField.getText();
        Double newSalary = Double.valueOf(salaryField.getText());
        Double newCommission = Double.valueOf(commissionField.getText());

        if (newName.isEmpty() || newSalary.isNaN()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter username and password");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "UPDATE cashiers SET cashier_name = ?, salary = ?, commission = ? WHERE cashier_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, newName);
            pstmt.setDouble(2, newSalary);

            if (newCommission.isNaN() || commissionField.getText().isEmpty() ){
                pstmt.setNull(3, Types.DOUBLE);
            } else {
                pstmt.setDouble(3,newCommission);
            }

            pstmt.setString(4, selectedUser.getCashierName());
            pstmt.executeUpdate();

            selectedUser.setCashierName(newName);
            selectedUser.setSalary(newSalary);
            selectedUser.setCommission(newCommission);
            loadCashierData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleDeleteButton(ActionEvent actionEvent) {
        Cashier selectedUser = cashierTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to delete");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM cashiers WHERE cashier_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, selectedUser.getCashierName());
            pstmt.executeUpdate();

//            userList.remove(selectedUser);
            loadCashierData(); // clear user list, kita ambil lg. maka tdk perlu remove selectedUser
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void clearFields() {
        nameField.clear();
        salaryField.clear();
        commissionField.clear();
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
