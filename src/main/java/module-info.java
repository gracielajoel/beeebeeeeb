module com.example.projectcafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.projectcafe to javafx.fxml;
    exports com.example.projectcafe;
    exports com.example.projectcafe.classes;
   // exports com.example.projectcafe.Cashier;
    //opens com.example.projectcafe.Cashier to javafx.fxml;
    //exports com.example.projectcafe.Owner;
    //opens com.example.projectcafe.Owner to javafx.fxml;
}