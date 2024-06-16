package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class OwnerReportController {
    private Stage stage;

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
    public void handleCashierButtonAction(ActionEvent actionEvent) {
    }

    public void handleBestSellingButtonAction(ActionEvent actionEvent) {
    }

    public void handleSalesButtonAction(ActionEvent actionEvent) {
    }

    public void handleActiveMemberButtonAction(ActionEvent actionEvent) {
    }
}
