package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
import com.example.projectcafe.classes.Member;
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

public class ManagePageMemberController {

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private TableColumn<Member,String> nameColumn;

    @FXML
    private TableColumn<Member, Double> pointColumn;

    @FXML
    private TableColumn<Member, String> birthdateColumn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TableColumn<Member, String> addressColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField pointField;

    @FXML
    private TextField birthdateField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    private Stage stage;
    private ObservableList<Member> memberList = FXCollections.observableArrayList();

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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("memberPoint"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        memberTable.setItems(memberList);
        loadMemberData();
    }
    private void loadMemberData() {
        memberList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM members";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                memberList.add(new Member(rs.getString("member_name"), rs.getInt("member_point"), rs.getString("birth_date"), rs.getString("email"), rs.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddButton(ActionEvent actionEvent) {
        String memberName = nameField.getText();
        Integer point = Integer.parseInt(pointField.getText());
        String birthdate = birthdateField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        if (memberName.isEmpty() || point.describeConstable().isEmpty() || birthdate.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter username and password");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO members (member_name, member_point, birth_date, email, address) VALUES (?, ?, CAST(? AS DATE), ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, memberName);
            pstmt.setInt(2, point);
            pstmt.setString(3, birthdate);

            if (email.isEmpty()){
                pstmt.setString(4,null);
            } else {
                pstmt.setString(4,email);
            }

            if (address.isEmpty()){
                pstmt.setString(5,null);
            } else {
                pstmt.setString(5,address);
            }

            pstmt.executeUpdate();
            memberList.add(new Member(memberName, point, birthdate, email, address));
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    protected void handleUpdateButton(ActionEvent actionEvent) {
        Member selectedUser = memberTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to update");
            return;
        }

        String newName = nameField.getText();
        Integer newPoint = Integer.parseInt(pointField.getText());
        String newBirthdate = birthdateField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();

        if (newName.isEmpty() || newPoint.describeConstable().isEmpty() || newBirthdate.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter username and password");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "UPDATE members SET member_name = ?, member_point = ?, birth_date = CAST(? AS DATE), email = ?, address = ? WHERE member_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPoint);

            pstmt.setString(3,newBirthdate);

            if (newEmail.isEmpty()){
                pstmt.setString(4,null);
            } else {
                pstmt.setString(4,newEmail);
            }

            if (newAddress.isEmpty()){
                pstmt.setString(5,null);
            } else {
                pstmt.setString(5,newAddress);
            }

            pstmt.setString(6, selectedUser.getMemberName());
            pstmt.executeUpdate();

            selectedUser.setMemberName(newName);
            selectedUser.setMemberPoint(newPoint);
            selectedUser.setBirthDate(newBirthdate);
            selectedUser.setEmail(newEmail);
            selectedUser.setAddress(newAddress);
            loadMemberData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleDeleteButton(ActionEvent actionEvent) {
        Member selectedUser = memberTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to delete");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM members WHERE member_name = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, selectedUser.getMemberName());
            pstmt.executeUpdate();

//            userList.remove(selectedUser);
            loadMemberData(); // clear user list, kita ambil lg. maka tdk perlu remove selectedUser
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        pointField.clear();
        birthdateField.clear();
        emailField.clear();
        addressField.clear();
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
}
