package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    protected void handleBestSellingButtonAction(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("product-report1.fxml"));
                Parent root = loader.load();

                ReportProduct1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("product-report1.fxml"));
                Parent root = loader.load();

                ReportProduct1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleSalesButtonAction(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report1.fxml"));
                Parent root = loader.load();

                ReportSale1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sale-report1.fxml"));
                Parent root = loader.load();

                ReportSale1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleActiveMemberButtonAction(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("member-report1.fxml"));
                Parent root = loader.load();

                ReportMember1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("member-report1.fxml"));
                Parent root = loader.load();

                ReportMember1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handlePromoUsageButtonAction(ActionEvent actionEvent) {
        if (currentRole.equals("cashier")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("promo-report1.fxml"));
                Parent root = loader.load();

                ReportPromo1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("cashier");
                controller.setCashier_name(cashier_name);

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("promo-report1.fxml"));
                Parent root = loader.load();

                ReportPromo1Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-cashier.fxml"));
            Parent root = loader.load();

            CashierTaskController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("cashier");
            controller.setCashier_name(cashier_name);

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
