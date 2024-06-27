package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class CashierReportController {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }
    private Stage stage;
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
    public void handleBestSellingButtonAction(ActionEvent actionEvent) {
    }

    public void handleSalesButtonAction(ActionEvent actionEvent) {
    }

    public void handleActiveMemberButtonAction(ActionEvent actionEvent) {
    }

    public void handlePromoUsageButtonAction(ActionEvent actionEvent) {
    }
}
