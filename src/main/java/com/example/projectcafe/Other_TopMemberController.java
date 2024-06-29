package com.example.projectcafe;

import com.example.projectcafe.classes.Member;
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

public class Other_TopMemberController {
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
    private TableView<Member> memberPointTable;

    @FXML
    private TableColumn<Member,String> nameColumn;

    @FXML
    private TableColumn<Member, Integer> pointColumn;

    private ObservableList<Member> memberList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("memberPoint"));
        memberPointTable.setItems(memberList);
        loadMemberData();
    }
    private void loadMemberData() {
        memberList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM members ORDER BY member_point DESC LIMIT 5";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                memberList.add(new Member(rs.getString("member_name"),
                        rs.getInt("member_point")));
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
