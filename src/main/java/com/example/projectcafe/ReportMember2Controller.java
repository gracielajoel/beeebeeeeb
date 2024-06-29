package com.example.projectcafe;

import com.example.projectcafe.classes.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReportMember2Controller {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TableView<Member> memberTableView;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, Integer> pointsColumn;

    @FXML
    private Button backButton;

    private Stage stage;
    private String currentRole;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    private ObservableList<Member> memberData = FXCollections.observableArrayList();

    public void initialize(List<Member> topMembers) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("memberPoint"));
        memberData.addAll(topMembers);
        memberTableView.setItems(memberData);
        backButton.setOnAction(event -> handleBack());
    }

    @FXML
    protected void handleBack() {
        System.out.println("Back button pressed");
        System.out.println("Current role: " + currentRole);

        if (currentRole == null) {
            System.err.println("currentRole is not initialized!");
            return;
        }

        if (currentRole.equals("cashier")) {
            try {
                System.out.println("Loading report-cashier.fxml");
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
                System.out.println("Loading report-owner.fxml");
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