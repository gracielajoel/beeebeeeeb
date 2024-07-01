package com.example.projectcafe;

import com.example.projectcafe.classes.Periode;
import com.example.projectcafe.classes.Promo;
import com.example.projectcafe.classes.PromoUsed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ReportPromo1Controller {
    private String cashier_name;

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    @FXML
    private TextField monthField;

    @FXML
    private TextField yearField;


    private Stage stage;
    private String currentRole;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    @FXML
    private TableView<Periode> periodTable;

    @FXML
    private TableColumn<Periode,String> monthColumn;

    @FXML
    private TableColumn<Periode, String> yearColumn;

    private ObservableList<Periode> periodeList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        periodTable.setItems(periodeList);
        loadPeriod();
    }
    private void loadPeriod() {
        periodeList.clear();
        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT EXTRACT(MONTH FROM p.periode_promo) AS month, EXTRACT(YEAR FROM p.periode_promo) AS year\n" +
                    "FROM promos p\n" +
                    "ORDER BY year DESC, month DESC;";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                int monthNumber = rs.getInt("month");
                String monthName = getMonthName(monthNumber);
                String year = rs.getString("year");
                periodeList.add(new Periode(monthName, year));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getMonthName(int monthNumber) {
        return new DateFormatSymbols().getMonths()[monthNumber - 1];
    }

    @FXML
    protected void handleSubmit(ActionEvent event) {
        String month = monthField.getText();
        String year = yearField.getText();

        try {
            if (isValidDate(month, year)) {
                List<PromoUsed> topPromos = fetchTopPromos(month, year);
                if (!topPromos.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("promo-report2.fxml"));
                    Parent root = loader.load();

                    ReportPromo2Controller controller = loader.getController();
                    controller.initialize(topPromos);
                    controller.setStage(stage);
                    controller.setCurrentRole(currentRole);


                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } else {
                    showAlert("No promos found for the given month and year.");
                }
            } else {
                showAlert("Invalid month or year. Please enter valid numbers.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Month: " + month + ", Year: " + year);
    }

    @FXML
    protected void handleBack(ActionEvent event) {
        if (currentRole.equals("cashier")) {
            try {
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

    private boolean isValidDate(String month, String year) {
        return (getMonthNumber(month) != -1 && isValidYear(year));
    }

    private boolean isValidYear(String year) {
        try {
            int y = Integer.parseInt(year);
            return y > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getMonthNumber(String monthName) {
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i + 1; // Month number is 1-based
            }
        }
        return -1; // Invalid month
    }

    private List<PromoUsed> fetchTopPromos(String month, String year) {
        List<PromoUsed> promos = new ArrayList<>();
        int monthNumber = getMonthNumber(month);
        if (monthNumber == -1) {
            return promos;
        }

        String query = """
                    SELECT P.promo_name, COUNT(D.*)
                    FROM detail_orders D JOIN promos P
                    ON ( D.promo_id = P.promo_id )
                    JOIN invoices I ON ( D.invoice_id = I.invoice_id )
                    WHERE EXTRACT(MONTH FROM I.date_time) = ?\s
                    AND EXTRACT(YEAR FROM I.date_time) = ?
                    GROUP BY D.promo_id, P.promo_name
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, monthNumber);
            stmt.setInt(2, Integer.parseInt(year));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String promoName = rs.getString("promo_name");
                int totalPoints = rs.getInt("count");
                promos.add(new PromoUsed(promoName, totalPoints));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promos;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}