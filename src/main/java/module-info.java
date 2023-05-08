module com.example.treeningpaevik {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires org.json;
    requires com.calendarfx.view;


    opens com.example.treeningpaevik to javafx.fxml;
    exports com.example.treeningpaevik;
}