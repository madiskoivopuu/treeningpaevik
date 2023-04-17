module com.example.treeningpaevik {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.treeningpaevik to javafx.fxml;
    exports com.example.treeningpaevik;
}