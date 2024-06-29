package com.example.projectcafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportOthersController {
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
    @FXML
    protected void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report-owner.fxml"));
            Parent root = loader.load();

            OwnerReportController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleMostSalesButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cashiersales.fxml"));
            Parent root = loader.load();

            Other_CashierSalesController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleTopMemberButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("top5member.fxml"));
            Parent root = loader.load();

            Other_TopMemberController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleMostPromoButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mostpromo_report.fxml"));
            Parent root = loader.load();

            Other_MostPromoController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBestSellerProdButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("top5product.fxml"));
            Parent root = loader.load();

            Other_TopProductController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentRole("owner");

            // ini utk ganti scene
            // root utk ngisi isi scene
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSalaryIncreaseButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("naikgaji.fxml"));
            Parent root = loader.load();

            Other_NaikGajiController controller = loader.getController();
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
