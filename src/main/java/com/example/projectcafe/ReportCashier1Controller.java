package com.example.projectcafe;

import com.example.projectcafe.classes.Cashier;
import com.example.projectcafe.classes.CashierSales;
import com.example.projectcafe.classes.Periode;
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
import java.util.ArrayList;
import java.util.List;

public class ReportCashier1Controller {


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
            String query = "SELECT DISTINCT TO_CHAR(date_time, 'FMMonth') AS month_name, EXTRACT(YEAR FROM date_time) AS year\n FROM invoices;";
            ResultSet rs = db.createStatement().executeQuery(query);

            while (rs.next()) {
                periodeList.add(new Periode(rs.getString("month_name"),
                        rs.getString("year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSubmit(ActionEvent event) throws SQLException {
        String month = monthField.getText();
        String year = yearField.getText();

        if (validatePeriod(month, year)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cashier-report2.fxml"));
                Parent root = loader.load();

                ReportCashier2Controller controller = loader.getController();
                controller.setStage(stage);
                controller.setCurrentRole("owner");
                String periodValue = month + " " + year;
                controller.setPeriod(periodValue);
                System.out.println("Period set to: " + periodValue); // Debugging statement

                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid period.");
            alert.showAndWait(); // If not closed, the program won't continue
        }
    }

    private boolean validatePeriod(String month, String year) throws SQLException {
        // ga perlu try catch krn udh ada sqlexception
        Connection db = DatabaseConnection.getConnection();
        String query = "SELECT DISTINCT TO_CHAR(date_time, 'FMMonth') || ' ' || EXTRACT(YEAR FROM date_time) AS month_year FROM invoices WHERE TO_CHAR(date_time, 'FMMonth') || ' ' || EXTRACT(YEAR FROM date_time) = ?";

        String period = month + " " + year;

        try (PreparedStatement pstmt = db.prepareStatement(query)) {
            // index 1 = ? yg pertama
            pstmt.setString(1, period);

            ResultSet result = pstmt.executeQuery();
            return (result.next());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    protected void handleBack(ActionEvent event) {
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

    private boolean isValidDate(String month, String year) {
        try {
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);
            return (m >= 1 && m <= 12 && y > 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<Cashier> fetchTopCashiers(String month, String year) {
        List<Cashier> cashiers = new ArrayList<>();

        String query = """
                    SELECT c.cashier_name, COUNT(i.invoice_id) AS invoice_count
                    FROM cashiers c
                    LEFT JOIN invoices i ON c.cashier_id = i.cashier_id
                    AND EXTRACT(MONTH FROM i.date_time) = ?
                    AND EXTRACT(YEAR FROM i.date_time) = ?
                    GROUP BY c.cashier_name
                    ORDER BY invoice_count DESC
                    LIMIT 5;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(month));
            stmt.setInt(2, Integer.parseInt(year));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String cashierName = rs.getString("cashier_name");
                int totalPoints = rs.getInt("invoice_count");
               // cashiers.add(new Cashier(cashierName, null, null, totalPoints));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cashiers;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}